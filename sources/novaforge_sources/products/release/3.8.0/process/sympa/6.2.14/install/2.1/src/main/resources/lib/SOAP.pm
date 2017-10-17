# -*- indent-tabs-mode: nil; -*-
# vim:ft=perl:et:sw=4
# $Id: SOAP.pm 12709 2016-02-26 16:11:14Z sympa-authors $

# Sympa - SYsteme de Multi-Postage Automatique
#
# Copyright (c) 1997, 1998, 1999 Institut Pasteur & Christophe Wolfhugel
# Copyright (c) 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005,
# 2006, 2007, 2008, 2009, 2010, 2011 Comite Reseau des Universites
# Copyright (c) 2011, 2012, 2013, 2014, 2015, 2016 GIP RENATER
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

package Sympa::SOAP;

use strict;
use warnings;
use Encode qw();

use Sympa;
use Sympa::Admin;
use Sympa::Auth;
use Conf;
use Sympa::Constants;
use Sympa::List;
use Sympa::Log;
use Sympa::Request;
use Sympa::Robot;
use Sympa::Scenario;
use Sympa::Session;
use Sympa::Spool::Auth;
use Sympa::Template;
use Sympa::Tools::Password;
use Sympa::Tools::Text;
use Sympa::User;

## Define types of SOAP type listType
my %types = (
    'listType' => {
        'listAddress'  => 'string',
        'homepage'     => 'string',
        'isSubscriber' => 'boolean',
        'isOwner'      => 'boolean',
        'isEditor'     => 'boolean',
        'subject'      => 'string',
        'email'        => 'string',
        'gecos'        => 'string'
    }
);

my $log = Sympa::Log->instance;

sub checkCookie {
    my $class = shift;

    my $sender = $ENV{'USER_EMAIL'};

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    $log->syslog('debug', 'SOAP checkCookie');

    return SOAP::Data->name('result')->type('string')->value($sender);
}

sub lists {
    my $self     = shift;    #$self is a service object
    my $topic    = shift;
    my $subtopic = shift;
    my $mode     = shift;
    $mode ||= '';

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    $log->syslog('notice', '(%s, %s, %s)', $topic, $subtopic, $sender);

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    my @result;

    $log->syslog('info', '(%s, %s)', $topic, $subtopic);

    my $all_lists = Sympa::List::get_lists($robot);
    foreach my $list (@$all_lists) {

        my $listname = $list->{'name'};

        my $result_item = {};
        my $result      = Sympa::Scenario::request_action(
            $list,
            'visibility',
            'md5',
            {   'sender'                  => $sender,
                'remote_application_name' => $ENV{'remote_application_name'}
            }
        );
########## NovaForge needs ########################################
	# add owners

	    my @owners = $list->get_owners_email();
        # add description and template
        my $description = $list->get_list_description();
        my $template = $list->get_list_template();
        # add subscribers
        my @subscribers;
        
		for (
			my $subscriber=$list->get_first_list_member(); 
			$subscriber; 
			$subscriber=$list->get_next_list_member()
		) {
		$log->syslog('debug', 'subscriber=(%s)', $subscriber->{'email'});
         push @subscribers, $subscriber->{'email'};
        }
		
########## NovaForge needs ########################################
        my $action;
        $action = $result->{'action'} 
		
		if (ref($result) eq 'HASH');
        next unless ($action eq 'do_it');

        ##building result packet
        $result_item->{'listAddress'} =
            $listname . '@' . $list->{'admin'}{'host'};
        $result_item->{'subject'} = $list->{'admin'}{'subject'};
        $result_item->{'subject'} =~ s/;/,/g;
        $result_item->{'homepage'} =
              Conf::get_robot_conf($robot, 'wwsympa_url') 
            . '/info/'
            . $listname;
########## NovaForge needs ########################################
	$result_item->{'owners'} = join(',', @owners);
	$result_item->{'description'} = $description;
	$result_item->{'template'} = $template;
	$result_item->{'subscribers'} = join(',', @subscribers);
########## NovaForge needs ########################################

        my $listInfo;
        if ($mode eq 'complex') {
            $listInfo = struct_to_soap($result_item);
        } else {
            $listInfo = struct_to_soap($result_item, 'as_string');
        }
	
        ## no topic ; List all lists
        if (!$topic) {
            push @result, $listInfo;

        } elsif ($list->{'admin'}{'topics'}) {
            foreach my $list_topic (@{$list->{'admin'}{'topics'}}) {
                my @tree = split '/', $list_topic;

                next if (($topic)    && ($tree[0] ne $topic));
                next if (($subtopic) && ($tree[1] ne $subtopic));

                push @result, $listInfo;
            }
        } elsif ($topic eq 'topicsless') {
            push @result, $listInfo;
        }
    }
    return SOAP::Data->name('listInfo')->value(\@result);
}

sub login {
    my $class  = shift;
    my $email  = shift;
    my $passwd = shift;

    my $http_host = $ENV{'SERVER_NAME'};
    my $robot     = $ENV{'SYMPA_ROBOT'};
    $log->syslog('notice', '(%s)', $email);

    #foreach my  $k (keys %ENV) {
    #$log->syslog('notice', 'ENV %s = %s', $k, $ENV{$k});
    #}
    unless (defined $http_host) {
        $log->syslog('err', 'SERVER_NAME not defined');
    }
    unless (defined $email) {
        $log->syslog('err', 'Email not defined');
    }
    unless (defined $passwd) {
        $log->syslog('err', 'Passwd not defined');
    }

    unless ($http_host and $email and $passwd) {
        $log->syslog('err', 'Incorrect number of parameters');
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <HTTP host> <email> <password>');
    }

    ## Authentication of the sender
    ## Set an env var to find out if in a SOAP context
    $ENV{'SYMPA_SOAP'} = 1;

    my $user = Sympa::Auth::check_auth($robot, $email, $passwd);

    unless ($user) {
        $log->syslog('notice', 'Login authentication failed');
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Authentication failed')
            ->faultdetail("Incorrect password for user $email or bad login");
    }

    ## Create Sympa::Session object
    my $session = Sympa::Session->new($robot,
        {'cookie' => Sympa::Session::encrypt_session_id($ENV{'SESSION_ID'})});
    $ENV{'USER_EMAIL'} = $email;
    $session->{'email'} = $email;
    $session->store();

    ## Note that id_session changes each time it is saved in the DB
    $ENV{'SESSION_ID'} = $session->{'id_session'};

    ## Also return the cookie value
    return SOAP::Data->name('result')->type('string')
        ->value(Sympa::Session::encrypt_session_id($ENV{'SESSION_ID'}));
}

sub casLogin {
    my $class       = shift;
    my $proxyTicket = shift;

    my $http_host = $ENV{'SERVER_NAME'};
    my $sender    = $ENV{'USER_EMAIL'};
    my $robot     = $ENV{'SYMPA_ROBOT'};
    $log->syslog('notice', '(%s)', $proxyTicket);

    unless ($http_host and $proxyTicket) {
        $log->syslog('err', 'Incorrect number of parameters');
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <HTTP host> <proxyTicket>');
    }

    unless (eval "require AuthCAS") {
        $log->syslog('err',
            "Unable to use AuthCAS library, install AuthCAS (CPAN) first");
        return undef;
    }
    require AuthCAS;

    ## Validate the CAS ST against all known CAS servers defined in auth.conf
    ## CAS server response will include the user's NetID
    my ($user, @proxies, $email, $cas_id);
    foreach my $service_id (0 .. $#{$Conf::Conf{'auth_services'}{$robot}}) {
        my $auth_service = $Conf::Conf{'auth_services'}{$robot}[$service_id];
        ## skip non CAS entries
        next
            unless ($auth_service->{'auth_type'} eq 'cas');

        my $cas = AuthCAS->new(
            casUrl => $auth_service->{'base_url'},
            #CAFile => '/usr/local/apache/conf/ssl.crt/ca-bundle.crt',
        );

        ($user, @proxies) =
            $cas->validatePT(Conf::get_robot_conf($robot, 'soap_url'),
            $proxyTicket);
        unless (defined $user) {
            $log->syslog(
                'err', 'CAS ticket %s not validated by server %s: %s',
                $proxyTicket, $auth_service->{'base_url'},
                AuthCAS::get_errors()
            );
            next;
        }

        $log->syslog('notice', 'User %s authenticated against server %s',
            $user, $auth_service->{'base_url'});

        ## User was authenticated
        $cas_id = $service_id;
        last;
    }

    unless ($user) {
        $log->syslog('notice', 'Login authentication failed');
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Authentication failed')
            ->faultdetail("Proxy ticket could not be validated");
    }

    ## Now fetch email attribute from LDAP
    unless ($email =
        Sympa::Auth::get_email_by_net_id($robot, $cas_id, {'uid' => $user})) {
        $log->syslog('err',
            'Could not get email address from LDAP for user %s', $user);
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Authentication failed')
            ->faultdetail("Could not get email address from LDAP directory");
    }

    ## Create Sympa::Session object
    my $session = Sympa::Session->new($robot,
        {'cookie' => Sympa::Session::encrypt_session_id($ENV{'SESSION_ID'})});
    $ENV{'USER_EMAIL'} = $email;
    $session->{'email'} = $email;
    $session->store();

    ## Note that id_session changes each time it is saved in the DB
    $ENV{'SESSION_ID'} = $session->{'id_session'};

    ## Also return the cookie value
    return SOAP::Data->name('result')->type('string')
        ->value(Sympa::Session::encrypt_session_id($ENV{'SESSION_ID'}));
}

## Used to call a service as an authenticated user without using HTTP cookies
## First parameter is the secret contained in the cookie
sub authenticateAndRun {
    my ($self, $email, $cookie, $service, $parameters) = @_;
    my $session_id;

    if ($parameters) {
        $log->syslog('notice', '(%s, %s, %s, %s)',
            $email, $cookie, $service, join(',', @$parameters));
    } else {
        $log->syslog('notice', '(%s, %s, %s)', $email, $cookie, $service);
    }

    unless ($cookie and $service) {
        $log->syslog('err', "Missing parameter");
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <email> <cookie> <service>');
    }

    ## Provided email is not trusted, we fetch the user email from the
    ## session_table instead
    my $session =
        Sympa::Session->new($ENV{'SYMPA_ROBOT'}, {'cookie' => $cookie});
    if (defined $session) {
        $email      = $session->{'email'};
        $session_id = $session->{'id_session'};
    }
    unless ($email or $email eq 'unknown') {
        $log->syslog('err', 'Failed to authenticate user with session ID %s',
            $session_id);
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Could not get email from cookie')->faultdetail('');
    }

    $ENV{'USER_EMAIL'} = $email;
    $ENV{'SESSION_ID'} = $session_id;

    no strict 'refs';
    $service->($self, @$parameters);
}
## request user email from http cookie
##
sub getUserEmailByCookie {
    my ($self, $cookie) = @_;

    $log->syslog('debug3', '(%s)', $cookie);

    unless ($cookie) {
        $log->syslog('err', "Missing parameter cookie");
        die SOAP::Fault->faultcode('Client')->faultstring('Missing parameter')
            ->faultdetail('Use : <cookie>');
    }

    my $session =
        Sympa::Session->new($ENV{'SYMPA_ROBOT'}, {'cookie' => $cookie});

    unless (defined $session && ($session->{'email'} ne 'unkown')) {
        $log->syslog('err', 'Failed to load session for %s', $cookie);
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Could not get email from cookie')->faultdetail('');
    }

    return SOAP::Data->name('result')->type('string')
        ->value($session->{'email'});

}
## Used to call a service from a remote proxy application
## First parameter is the application name as defined in the
## trusted_applications.conf file
##   2nd parameter is remote application password
##   3nd a string with multiple cars definition comma separated
##   (var=value,var=value,...)
##   4nd is service name requested
##   5nd service parameters
sub authenticateRemoteAppAndRun {
    my ($self, $appname, $apppassword, $vars, $service, $parameters) = @_;
    my $robot = $ENV{'SYMPA_ROBOT'};

    if ($parameters) {
        $log->syslog('notice', '(%s, %s, %s, %s)',
            $appname, $vars, $service, join(',', @$parameters));
    } else {
        $log->syslog('notice', '(%s, %s, %s)', $appname, $vars, $service);
    }

    unless ($appname and $apppassword and $service) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <appname> <apppassword> <vars> <service>');
    }
    my ($proxy_vars, $set_vars) =
        Sympa::Auth::remote_app_check_password($appname, $apppassword, $robot,
        $service);

    unless (defined $proxy_vars) {
        $log->syslog('notice', 'Authentication failed');
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Authentication failed')
            ->faultdetail("Authentication failed for application $appname");
    }
    $ENV{'remote_application_name'} = $appname;

    foreach my $var (split(/,/, $vars)) {
        # check if the remote application is trusted proxy for this variable
        # $log->syslog('notice',
        #     'Remote application is trusted proxy for %s', $var);

        my ($id, $value) = split(/=/, $var);
        if (!defined $id) {
            $log->syslog('notice', 'Incorrect syntaxe ID');
            die SOAP::Fault->faultcode('Server')
                ->faultstring('Incorrect syntaxe id')
                ->faultdetail("Unrecognized syntaxe  $var");
        }
        if (!defined $value) {
            $log->syslog('notice', 'Incorrect syntaxe value');
            die SOAP::Fault->faultcode('Server')
                ->faultstring('Incorrect syntaxe value')
                ->faultdetail("Unrecognized syntaxe  $var");
        }
        $ENV{$id} = $value if ($proxy_vars->{$id});
    }
    # Set fixed variables.
    foreach my $var (keys %$set_vars) {
        $ENV{$var} = $set_vars->{$var};
    }

    no strict 'refs';
    $service->($self, @$parameters);
}

sub amI {
    my ($class, $listname, $function, $user) = @_;

    my $robot = $ENV{'SYMPA_ROBOT'};

    $log->syslog('notice', '(%s, %s, %s)', $listname, $function, $user);

    unless ($listname and $user and $function) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list> <function> <user email>');
    }

    $listname = lc($listname);
    my $list = Sympa::List->new($listname, $robot);

    $log->syslog('debug', '(%s)', $listname);

    if ($list) {
        if ($function eq 'subscriber') {
            return SOAP::Data->name('result')->type('boolean')
                ->value($list->is_list_member($user));
        } elsif ($function eq 'editor') {
            return SOAP::Data->name('result')->type('boolean')
                ->value($list->is_admin('actual_editor', $user));
        } elsif ($function eq 'owner') {
            return SOAP::Data->name('result')->type('boolean')
                ->value($list->is_admin('owner', $user)
                    || Sympa::is_listmaster($list, $user));
        } else {
            die SOAP::Fault->faultcode('Server')
                ->faultstring('Unknown function.')
                ->faultdetail("Function $function unknown");
        }
    } else {
        die SOAP::Fault->faultcode('Server')->faultstring('Unknown list.')
            ->faultdetail("List $listname unknown");
    }

}

sub info {
    my $class    = shift;
    my $listname = shift;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }

    $log->syslog('notice', '(%s)', $listname);

    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info', 'Info %s from %s refused, list unknown',
            $listname, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Unknown list')
            ->faultdetail("List $listname unknown");
    }

    my $result = Sympa::Scenario::request_action(
        $list, 'info', 'md5',
        {   'sender'                  => $sender,
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );
    my $action;
    $action = $result->{'action'} if (ref($result) eq 'HASH');

    die SOAP::Fault->faultcode('Server')->faultstring('No action available')
        unless (defined $action);

    if ($action =~ /reject/i) {
        my $reason_string = get_reason_string($result->{'reason'}, $robot);
        $log->syslog('info', 'Info %s from %s refused (not allowed)',
            $listname, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Not allowed')
            ->faultdetail($reason_string);
    }
    if ($action =~ /do_it/i) {
        my $result_item;

        $result_item->{'listAddress'} =
            SOAP::Data->name('listAddress')->type('string')
            ->value($listname . '@' . $list->{'admin'}{'host'});
        $result_item->{'subject'} =
            SOAP::Data->name('subject')->type('string')
            ->value($list->{'admin'}{'subject'});
        $result_item->{'homepage'} =
            SOAP::Data->name('homepage')->type('string')
            ->value(Conf::get_robot_conf($robot, 'wwsympa_url') 
                . '/info/'
                . $listname);

        ## determine status of user
        if ($list->is_admin('owner', $sender)
            or Sympa::is_listmaster($list, $sender)) {
            $result_item->{'isOwner'} =
                SOAP::Data->name('isOwner')->type('boolean')->value(1);
        }
        if ($list->is_admin('actual_editor', $sender)) {
            $result_item->{'isEditor'} =
                SOAP::Data->name('isEditor')->type('boolean')->value(1);
        }
        if ($list->is_list_member($sender)) {
            $result_item->{'isSubscriber'} =
                SOAP::Data->name('isSubscriber')->type('boolean')->value(1);
        }

        #push @result, SOAP::Data->type('listType')->value($result_item);
        return SOAP::Data->value([$result_item]);
    }
    $log->syslog('info',
        'Info %s from %s aborted, unknown requested action in scenario',
        $listname, $sender);
    die SOAP::Fault->faultcode('Server')
        ->faultstring('Unknown requested action')->faultdetail(
        "SOAP info : %s from %s aborted because unknown requested action in scenario",
        $listname, $sender
        );
}


sub existList {
    my $class = shift;
    my $listname  = shift;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot = $ENV{'SYMPA_ROBOT'};
    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog('info', 'SOAP existList(list = %s\@%s,robot = %s) from %s via proxy application %s', $listname,$robot,$sender,$remote_application_name);

    unless ($sender) {
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('User not specified')
	    ->faultdetail('Use a trusted proxy or login first ');
    }

    my $result = "KO";

    unless ($listname) {
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters')
	    ->faultdetail('Use : <list>');
    }
	
    $log->syslog('debug', 'SOAP create_list(%s,%s)', $listname,$robot);

	my $list = Sympa::List->new($listname, $robot);
    if ($list) {
	$result = "OK";
    }
    return $result;
}

sub createList {
    my $class       = shift;
    my $listname    = shift;
    my $subject     = shift;
    my $list_tpl    = shift;
    my $description = shift;
    my $topics      = shift;

    my $sender                  = $ENV{'USER_EMAIL'};
    my $robot                   = $ENV{'SYMPA_ROBOT'};
    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog(
        'info',
        '(list = %s\@%s, subject = %s, template = %s, description = %s, topics = %s) From %s via proxy application %s',
        $listname,
        $robot,
        $subject,
        $list_tpl,
        $description,
        $topics,
        $sender,
        $remote_application_name
    );

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not specified')
            ->faultdetail('Use a trusted proxy or login first ');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }

    # Check length.
    if (Sympa::Constants::LIST_LEN() < length $listname) {
        die SOAP::Fault->faultcode('Client')->faultstring('Too long value')
            ->faultdetail('List name is too long');
    }

    $log->syslog('debug', '(%s, %s)', $listname, $robot);

    my $list = Sympa::List->new($listname, $robot);
    if ($list) {
        $log->syslog('info',
            'Create_list %s@%s from %s refused, list already exist',
            $listname, $robot, $sender);
        die SOAP::Fault->faultcode('Client')
            ->faultstring('List already exists')
            ->faultdetail("List $listname already exists");
    }

    my $reject;
    unless ($subject) {
        $reject .= 'subject';
    }
    unless ($list_tpl) {
        $reject .= ', template';
    }
    unless ($description) {
        $reject .= ', description';
    }
    unless ($topics) {
        $reject .= 'topics';
    }
    if ($reject) {
        $log->syslog('info',
            'Create_list %s@%s from %s refused, missing parameter(s) %s',
            $listname, $robot, $sender, $reject);
        die SOAP::Fault->faultcode('Server')->faultstring('Missing parameter')
            ->faultdetail("Missing required parameter(s) : $reject");
    }
    # check authorization
    my $result = Sympa::Scenario::request_action(
        $robot,
        'create_list',
        'md5',
        {   'sender'                  => $sender,
            'candidate_listname'      => $listname,
            'candidate_subject'       => $subject,
            'candidate_template'      => $list_tpl,
            'candidate_info'          => $description,
            'candidate_topics'        => $topics,
            'remote_host'             => $ENV{'REMOTE_HOST'},
            'remote_addr'             => $ENV{'REMOTE_ADDR'},
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );
    my $r_action;
    my $reason;
    if (ref($result) eq 'HASH') {
        $r_action = $result->{'action'};
        $reason   = $result->{'reason'};
    }
    unless ($r_action =~ /do_it|listmaster/) {
        $log->syslog('info', 'Create_list %s@%s from %s refused, reason %s',
            $listname, $robot, $sender, $reason);
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Authorization reject')
            ->faultdetail("Authorization reject : $reason");
    }

    # prepare parameters
    my $param = {};
    $param->{'user'}{'email'} = $sender;
    if (Sympa::User::is_global_user($param->{'user'}{'email'})) {
        $param->{'user'} = Sympa::User::get_global_user($sender);
    }
    my $parameters;
    $parameters->{'creation_email'} = $sender;
    my %owner;
    $owner{'email'} = $param->{'user'}{'email'};
    $owner{'gecos'} = $param->{'user'}{'gecos'};
    push @{$parameters->{'owner'}}, \%owner;

    $parameters->{'listname'}    = $listname;
    $parameters->{'subject'} = &Encode::encode($Conf::Conf{'filesystem_encoding'}, $subject);
    $parameters->{'description'} = &Encode::encode($Conf::Conf{'filesystem_encoding'}, $description);
    $parameters->{'topics'}      = $topics;

    if ($r_action =~ /listmaster/i) {
        $param->{'status'} = 'pending';
    } elsif ($r_action =~ /do_it/i) {
        $param->{'status'} = 'open';
    }

    ## create liste
    my $resul =
        Sympa::Admin::create_list_old($parameters, $list_tpl, $robot, "soap");
    unless (defined $resul
        and $list = Sympa::List->new($listname, $robot)) {
        $log->syslog('info', 'Unable to create list %s@%s from %s',
            $listname, $robot, $sender);
        die SOAP::Fault->faultcode('Server')
            ->faultstring('unable to create list')
            ->faultdetail('unable to create list');
    }

    ## notify listmaster
    if ($param->{'create_action'} =~ /notify/) {
        if (Sympa::send_notify_to_listmaster(
                $list, 'request_list_creation', {'email' => $sender}
            )
            ) {
            $log->syslog('info', 'Notify listmaster for list creation');
        }
    }
    return "OK";

}

sub closeList {
    my $class    = shift;
    my $listname = shift;

    my $sender                  = $ENV{'USER_EMAIL'};
    my $robot                   = $ENV{'SYMPA_ROBOT'};
    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog('info', '(list = %s\@%s) From %s via proxy application %s',
        $listname, $robot, $sender, $remote_application_name);

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not specified')
            ->faultdetail('Use a trusted proxy or login first ');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }

    $log->syslog('debug', '(%s, %s)', $listname, $robot);

    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info', 'CloseList %s@%s from %s refused, unknown list',
            $listname, $robot, $sender);
        die SOAP::Fault->faultcode('Client')->faultstring('unknown list')
            ->faultdetail("inknown list $listname");
    }

    # check authorization
    unless ($list->is_admin('owner', $sender)
        or Sympa::is_listmaster($list, $sender)) {
        $log->syslog('info', 'CloseList %s from %s not allowed',
            $listname, $sender);
        die SOAP::Fault->faultcode('Client')->faultstring('Not allowed')
            ->faultdetail("Not allowed");
    }

    if ($list->{'admin'}{'status'} eq 'closed') {
        $log->syslog('info', 'Already closed');
        die SOAP::Fault->faultcode('Client')
            ->faultstring('list allready closed')
            ->faultdetail("list $listname all ready closed");
    } elsif ($list->{'admin'}{'status'} eq 'pending') {
        $log->syslog('info', 'Closing a pending list makes it purged');
        $list->purge($sender);
    } else {
        $list->close_list($sender);
        $log->syslog('info', 'List %s closed', $listname);
    }
     return "OK";
}

sub add {
    my $class    = shift;
    my $listname = shift;
    my $email    = shift;
    my $gecos    = shift;
    my $quiet    = shift;

    my $sender                  = $ENV{'USER_EMAIL'};
    my $robot                   = $ENV{'SYMPA_ROBOT'};
    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog(
        'info',
        '(list = %s@%s, email = %s, quiet = %s) From %s via proxy application %s',
        $listname,
        $robot,
        $email,
        $quiet,
        $sender,
        $remote_application_name
    );

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not specified')
            ->faultdetail('Use a trusted proxy or login first ');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }
    unless ($email) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <email>');
    }
    unless (Sympa::Tools::Text::valid_email($email)) {
        my $error = "Invalid email address provided: '$email'";
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Unable to add user')->faultdetail($error);
    }
    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info', 'Add %s@%s %s from %s refused, no such list',
            $listname, $robot, $email, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Undefined list')
            ->faultdetail("Undefined list");
    }

    # check authorization

    my $result = Sympa::Scenario::request_action(
        $list, 'add', 'md5',
        {   'sender'                  => $sender,
            'email'                   => $email,
            'remote_host'             => $ENV{'REMOTE_HOST'},
            'remote_addr'             => $ENV{'REMOTE_ADDR'},
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );

    my $action;
    my $reason;
    if (ref($result) eq 'HASH') {
        $action = $result->{'action'};
        $reason = $result->{'reason'};
    }

    unless (defined $action) {
        $log->syslog('info', 'Add %s@%s %s from %s: scenario error',
            $listname, $robot, $email, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('scenario error')
            ->faultdetail(
            "sender $sender email $email remote $ENV{'remote_application_name'} "
            );
    }

    unless ($action =~ /do_it/) {
        my $reason_string = get_reason_string($reason, $robot);
        $log->syslog('info', 'Add %s@%s %s from %s refused (not allowed)',
            $listname, $robot, $email, $sender);
        die SOAP::Fault->faultcode('Client')->faultstring('Not allowed')
            ->faultdetail($reason_string);
    }

    if ($list->is_list_member($email)) {
        $log->syslog('err',
            'Add %s@%s %s from %s: Failed, user already member of the list',
            $listname, $robot, $email, $sender);
        my $error = "User already member of list $listname";
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Unable to add user')->faultdetail($error);

    } else {
        my $u;
        my $defaults = $list->get_default_user_options();
        my $u2       = Sympa::User->new($email);
        %{$u} = %{$defaults};
        $u->{'email'} = $email;
        $u->{'gecos'} = $gecos || $u2->gecos;
        $u->{'date'}  = $u->{'update_date'} = time;

        # If Password validation is enabled check the submitted password
        # against the site configured constraints
        if ($u2->{'password'}) {
            if (my $result =
                Sympa::Tools::Password::password_validation($u->{'password'}))
            {
                $log->syslog('info', 'add %s@%s %s from %s : scenario error',
                    $listname, $robot, $email, $sender);
                die SOAP::Fault->faultcode('Server')
                    ->faultstring('Weak password')
                    ->faultdetail('Weak password: ' . $result);
            }
            $u->{'password'} = $u2->{'password'};
        } else {
            $u->{'password'} = Sympa::Tools::Password::tmp_passwd($email);
        }

        $u->{'lang'} = $u2->lang || $list->{'admin'}{'lang'};

        $list->add_list_member($u);
        if (defined $list->{'add_outcome'}{'errors'}) {
            $log->syslog('info', 'Add %s@%s %s from %s: Unable to add user',
                $listname, $robot, $email, $sender);
            my $error = sprintf "Unable to add user %s in list %s: %s",
                $email, $listname,
                $list->{'add_outcome'}{'errors'}{'error_message'};
            die SOAP::Fault->faultcode('Server')
                ->faultstring('Unable to add user')->faultdetail($error);
        }

        my $spool_req = Sympa::Spool::Auth->new(
            context => $list,
            email   => $email,
            action  => 'add'
        );
        while (1) {
            my ($request, $handle) = $spool_req->next;
            last unless $handle;
            next unless $request;

            $spool_req->remove($handle);
        }
    }

    ## Now send the welcome file to the user if it exists and notification is
    ## supposed to be sent.
    unless ($quiet || $action =~ /quiet/i) {
        unless ($list->send_probe_to_user('welcome', $email)) {
            $log->syslog('notice', 'Unable to send "welcome" probe to %s',
                $email);
        }
    }

    $log->syslog('info', 'ADD %s %s from %s accepted (%d subscribers)',
        $list->{'name'}, $email, $sender, $list->get_total());
    if ($action =~ /notify/i) {
        $list->send_notify_to_owner(
            'notice',
            {   'who'     => $email,
                'gecos'   => $gecos,
                'command' => 'add',
                'by'      => $sender
            }
        );
    }
	
     return "OK";
}

sub del {
    my $class    = shift;
    my $listname = shift;
    my $email    = shift;
    my $quiet    = shift;

    my $sender                  = $ENV{'USER_EMAIL'};
    my $robot                   = $ENV{'SYMPA_ROBOT'};
    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog(
        'info',
        '(list = %s@%s, email = %s, quiet = %s) From %s via proxy application %s',
        $listname,
        $robot,
        $email,
        $quiet,
        $sender,
        $remote_application_name
    );

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not specified')
            ->faultdetail('Use a trusted proxy or login first ');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }
    unless ($email) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <email>');
    }
    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info', 'Del %s@%s %s from %s refused, no such list',
            $listname, $robot, $email, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Undefined list')
            ->faultdetail("Undefined list");
    }

    # check authorization

    my $result = Sympa::Scenario::request_action(
        $list, 'del', 'md5',
        {   'sender'                  => $sender,
            'email'                   => $email,
            'remote_host'             => $ENV{'REMOTE_HOST'},
            'remote_addr'             => $ENV{'REMOTE_ADDR'},
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );

    my $action;
    my $reason;
    if (ref($result) eq 'HASH') {
        $action = $result->{'action'};
        $reason = $result->{'reason'};
    }

    unless (defined $action) {
        $log->syslog('info', 'Del %s@%s %s from %s: scenario error',
            $listname, $robot, $email, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('scenario error')
            ->faultdetail(
            "sender $sender email $email remote $ENV{'remote_application_name'} "
            );
    }

    unless ($action =~ /do_it/) {
        my $reason_string = get_reason_string($reason, $robot);
        $log->syslog('info',
            'Del %s@%s %s from %s by %srefused (not allowed)',
            $listname, $robot, $email, $sender,
            $ENV{'remote_application_name'});
        die SOAP::Fault->faultcode('Client')->faultstring('Not allowed')
            ->faultdetail($reason_string);
    }

    my $user_entry = $list->get_list_member($email);
    unless ((defined $user_entry)) {
        $log->syslog('info', 'DEL %s %s from %s refused, not on list',
            $listname, $email, $sender);
        die SOAP::Fault->faultcode('Client')->faultstring('Not subscribed')
            ->faultdetail('Not member of list or not subscribed');
    }

    # Really delete and rewrite to disk.
    my $u;
    unless (
        $u = $list->delete_list_member(
            'users'     => [$email],
            'exclude'   => '0',
            'operation' => 'del'
        )
        ) 
		{
        my $error =
            "Unable to delete user $email from list $listname for command 'del'";
        $log->syslog('info', 'DEL %s %s from %s failed, ' . $error);
        die SOAP::Fault->faultcode('Server')
            ->faultstring('Unable to remove subscriber information')
            ->faultdetail('Database access failed');
    } else {
        my $spool_req = Sympa::Spool::Auth->new(
            context => $list,
            email   => $email,
            action  => 'del'
        );
        while (1) {
            my ($request, $handle) = $spool_req->next;
            last unless $handle;
            next unless $request;

            $spool_req->remove($handle);
        }
    }

    ## Send a notice to the removed user, unless the owner indicated
    ## quiet del.
    unless ($quiet || $action =~ /quiet/i) {
        unless (
            Sympa::send_file(
                $list, 'removed',
                $email, {'auto_submitted' => 'auto-generated'}
            )
            ) {
            $log->syslog('notice', 'Unable to send template "removed" to %s',
                $email);
        }
    }

    $log->syslog('info', 'DEL %s %s from %s accepted (%d subscribers)',
        $listname, $email, $sender, $list->get_total());
    if ($action =~ /notify/i) {
        $list->send_notify_to_owner(
            'notice',
            {   'who'     => $email,
                'gecos'   => "",
                'command' => 'del',
                'by'      => $sender
            }
        );
    }
    return "OK";
}

sub review {
    my $class    = shift;
    my $listname = shift;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    my @resultSoap;

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }

    $log->syslog('debug', '(%s, %s)', $listname, $robot);

    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info',
            'Review %s from %s refused, list unknown to robot %s',
            $listname, $sender, $robot);
        die SOAP::Fault->faultcode('Server')->faultstring('Unknown list')
            ->faultdetail("List $listname unknown");
    }

    my $user;

    # Part of the authorization code
    $user = Sympa::User::get_global_user($sender);

    my $result = Sympa::Scenario::request_action(
        $list, 'review', 'md5',
        {   'sender'                  => $sender,
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );
    my $action;
    $action = $result->{'action'} if (ref($result) eq 'HASH');

    die SOAP::Fault->faultcode('Server')->faultstring('No action available')
        unless (defined $action);

    if ($action =~ /reject/i) {
        my $reason_string = get_reason_string($result->{'reason'}, $robot);
        $log->syslog('info', 'Review %s from %s refused (not allowed)',
            $listname, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Not allowed')
            ->faultdetail($reason_string);
    }
    if ($action =~ /do_it/i) {
        my $is_owner = $list->is_admin('owner', $sender)
            || Sympa::is_listmaster($list, $sender);

        ## Members list synchronization if include is in use
        if ($list->has_include_data_sources()) {
            unless (defined $list->on_the_fly_sync_include(use_ttl => 1)) {
                $log->syslog('notice', 'Unable to synchronize list %s',
                    $list);
            }
        }
        unless ($user = $list->get_first_list_member({'sortby' => 'email'})) {
            $log->syslog('err', 'No subscribers in list "%s"',
                $list->{'name'});
            push @resultSoap,
                SOAP::Data->name('result')->type('string')
                ->value('no_subscribers');
            return SOAP::Data->name('return')->value(\@resultSoap);
        }
        do {
            ## Owners bypass the visibility option
            unless (($user->{'visibility'} eq 'conceal')
                and (!$is_owner)) {

                ## Lower case email address
                $user->{'email'} =~ y/A-Z/a-z/;
                push @resultSoap,
                    SOAP::Data->name('item')->type('string')
                    ->value($user->{'email'});
            }
        } while ($user = $list->get_next_list_member());
        $log->syslog('info', 'Review %s from %s accepted', $listname,
            $sender);
        return SOAP::Data->name('return')->value(\@resultSoap);
    }
    $log->syslog('info',
        'Review %s from %s aborted, unknown requested action in scenario',
        $listname, $sender);
    die SOAP::Fault->faultcode('Server')
        ->faultstring('Unknown requested action')->faultdetail(
        "SOAP review : %s from %s aborted because unknown requested action in scenario",
        $listname, $sender
        );
}

sub fullReview {
    my $class    = shift;
    my $listname = shift;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }

    $log->syslog('debug', '(%s, %s)', $listname, $robot);

    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info',
            'Review %s from %s refused, list unknown to robot %s',
            $listname, $sender, $robot);
        die SOAP::Fault->faultcode('Server')->faultstring('Unknown list')
            ->faultdetail("List $listname unknown");
    }

    unless (Sympa::is_listmaster($list, $sender)
        or $list->is_admin('owner', $sender)) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Not enough privileges')
            ->faultdetail('Listmaster or listowner required');
    }

    # Members list synchronization if include is in use
    if ($list->has_include_data_sources()) {
        unless (defined $list->on_the_fly_sync_include(use_ttl => 1)) {
            $log->syslog('notice', 'Unable to synchronize list %s', $list);
        }
    }

    my $members;
    my $user;
    if ($user = $list->get_first_list_member({'sortby' => 'email'})) {
        do {
            $user->{'email'} =~ y/A-Z/a-z/;

            my $res;
            $res->{'email'}        = $user->{'email'};
            $res->{'gecos'}        = $user->{'gecos'};
            $res->{'isOwner'}      = 0;
            $res->{'isEditor'}     = 0;
            $res->{'isSubscriber'} = 0;
            if ($list->is_list_member($user->{'email'})) {
                $res->{'isSubscriber'} = 1;
            }

            $members->{$user->{'email'}} = $res;
        } while ($user = $list->get_next_list_member());
    }

    foreach my $role (qw(owner editor)) {
        foreach my $user ($list->get_admins($role)) {
            $user->{'email'} =~ y/A-Z/a-z/;

            unless (defined $members->{$user->{'email'}}) {
                $members->{$user->{'email'}} = {
                    email        => $user->{'email'},
                    gecos        => $user->{'gecos'},
                    isOwner      => 0,
                    isEditor     => 0,
                    isSubscriber => 0,
                };
            }
            $members->{$user->{'email'}}{'isOwner'}  = 1 if $role eq 'owner';
            $members->{$user->{'email'}}{'isEditor'} = 1 if $role eq 'editor';
        }
    }

    my @result;
    foreach my $email (keys %$members) {
        push @result, struct_to_soap($members->{$email});
    }

    $log->syslog('info', 'FullReview %s from %s accepted', $listname,
        $sender);
    return SOAP::Data->name('return')->value(\@result);
}

sub signoff {
    my ($class, $listname) = @_;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    $log->syslog('notice', '(%s, %s)', $listname, $sender);

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters.')
            ->faultdetail('Use : <list> ');
    }

    my $list = Sympa::List->new($listname, $robot);

    ## Is this list defined
    unless ($list) {
        $log->syslog('info', 'Sign off from %s for %s refused, list unknown',
            $listname, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Unknown list.')
            ->faultdetail("List $listname unknown");
    }

    my $host = Conf::get_robot_conf($robot, 'host');

    if ($listname eq '*') {
        my $success;
        foreach my $list (Sympa::List::get_which($sender, $robot, 'member')) {
            my $l = $list->{'name'};

            $success ||= signoff($l, $sender);
        }
        return SOAP::Data->name('result')->value($success);
    }

    $list = Sympa::List->new($listname, $robot);

    my $result = Sympa::Scenario::request_action(
        $list,
        'unsubscribe',
        'md5',
        {   'email'                   => $sender,
            'sender'                  => $sender,
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );
    my $action;
    $action = $result->{'action'} if (ref($result) eq 'HASH');

    die SOAP::Fault->faultcode('Server')->faultstring('No action available.')
        unless (defined $action);

    if ($action =~ /reject/i) {
        my $reason_string = get_reason_string($result->{'reason'}, $robot);
        $log->syslog(
            'info',
            'Sign off from %s for the email %s of the user %s refused (not allowed)',
            $listname,
            $sender,
            $sender
        );
        die SOAP::Fault->faultcode('Server')->faultstring('Not allowed.')
            ->faultdetail($reason_string);
    }
    if ($action =~ /do_it/i) {
        ## Now check if we know this email on the list and
        ## remove it if found, otherwise just reject the
        ## command.
        unless ($list->is_list_member($sender)) {
            $log->syslog('info', 'Sign off %s from %s refused, not on list',
                $listname, $sender);

            ## Tell the owner somebody tried to unsubscribe
            if ($action =~ /notify/i) {
                $list->send_notify_to_owner('warn-signoff',
                    {'who' => $sender});
            }
            die SOAP::Fault->faultcode('Server')->faultstring('Not allowed.')
                ->faultdetail(
                "Email address $sender has not been found on the list $list->{'name'}. You did perhaps subscribe using a different address ?"
                );
        }

        ## Really delete and rewrite to disk.
        $list->delete_list_member(
            'users'     => [$sender],
            'exclude'   => '1',
            'operation' => 'signoff'
        );

        ## Notify the owner
        if ($action =~ /notify/i) {
            $list->send_notify_to_owner(
                'notice',
                {   'who'     => $sender,
                    'command' => 'signoff'
                }
            );
        }

        ## Send bye.tpl to sender
        unless (Sympa::send_file($list, 'bye', $sender, {})) {
            $log->syslog('err', 'Unable to send template "bye" to %s',
                $sender);
        }

        $log->syslog('info', 'Sign off %s from %s accepted',
            $listname, $sender);

        return SOAP::Data->name('result')->type('boolean')->value(1);
    }

    $log->syslog('info',
        'Sign off %s from %s aborted, unknown requested action in scenario',
        $listname, $sender);
    die SOAP::Fault->faultcode('Server')->faultstring('Undef')->faultdetail(
        "Sign off %s from %s aborted because unknown requested action in scenario",
        $listname, $sender
    );
}

sub subscribe {
    my ($class, $listname, $gecos) = @_;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    $log->syslog('info', '(%s, %s, %s)', $listname, $sender, $gecos);

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list> [user gecos]');
    }

    $log->syslog('notice', '(%s, %s)', $listname, $sender);

    ## Load the list if not already done, and reject the
    ## subscription if this list is unknown to us.
    my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
        $log->syslog('info',
            'Subscribe to %s from %s refused, list unknown to robot %s',
            $listname, $sender, $robot);
        die SOAP::Fault->faultcode('Server')->faultstring('Unknown list')
            ->faultdetail("List $listname unknown");
    }

    ## This is a really minimalistic handling of the comments,
    ## it is far away from RFC-822 completeness.
    $gecos =~ s/"/\\"/g;
    $gecos = "\"$gecos\"" if ($gecos =~ /[<>\(\)]/);

    ## query what to do with this subscribtion request
    my $result = Sympa::Scenario::request_action(
        $list,
        'subscribe',
        'md5',
        {   'sender'                  => $sender,
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );
    my $action;
    $action = $result->{'action'} if (ref($result) eq 'HASH');

    die SOAP::Fault->faultcode('Server')->faultstring('No action available.')
        unless (defined $action);

    $log->syslog('debug2', 'SOAP subscribe action: %s', $action);

    if ($action =~ /reject/i) {
        my $reason_string = get_reason_string($result->{'reason'}, $robot);
        $log->syslog('info',
            'SOAP subscribe to %s from %s refused (not allowed)',
            $listname, $sender);
        die SOAP::Fault->faultcode('Server')->faultstring('Not allowed.')
            ->faultdetail($reason_string);
    }
    if ($action =~ /owner/i) {
        $list->send_notify_to_owner(
            'subrequest',
            {   'who'     => $sender,
                'keyauth' => Sympa::compute_auth(
                    context => $list,
                    email   => $sender,
                    action  => 'add'
                ),
                'replyto' => Conf::get_robot_conf($robot, 'sympa'),
                'gecos'   => $gecos
            }
        );

        my $spool_req = Sympa::Spool::Auth->new;
        my $request   = Sympa::Request->new_from_tuples(
            context => $list,
            email   => $sender,
            gecos   => $gecos,
            action  => 'add'
        );
        $spool_req->store($request);

        $log->syslog('info', '%s from %s forwarded to the owners of the list',
            $listname, $sender);
        return SOAP::Data->name('result')->type('boolean')->value(1);
    }
    if ($action =~ /request_auth/i) {
        Sympa::request_auth(
            context => $list,
            sender  => $sender,
            action  => 'subscribe',
            gecos   => $gecos
        );
        $log->syslog('info', '%s from %s, auth requested', $listname,
            $sender);
        return SOAP::Data->name('result')->type('boolean')->value(1);
    }
    if ($action =~ /do_it/i) {

        my $is_sub = $list->is_list_member($sender);

        unless (defined($is_sub)) {
            $log->syslog('err', 'User lookup failed');
            die SOAP::Fault->faultcode('Server')->faultstring('Undef')
                ->faultdetail("SOAP subscribe : user lookup failed");
        }

        if ($is_sub) {
            # Only updates the date.  Options remain the same.
            my %update = (update_date => time);
            $update{gecos} = $gecos if defined $gecos and $gecos =~ /\S/;

            unless ($list->update_list_member($sender, %update)) {
                $log->syslog('err', 'User update failed');
                die SOAP::Fault->faultcode('Server')->faultstring('Undef.')
                    ->faultdetail("SOAP subscribe : update user failed");
            }
        } else {
            my $u;
            my $defaults = $list->get_default_user_options();
            %{$u} = %{$defaults};
            $u->{'email'} = $sender;
            $u->{'gecos'} = $gecos;
            $u->{'date'}  = $u->{'update_date'} = time;

            die SOAP::Fault->faultcode('Server')->faultstring('Undef')
                ->faultdetail("SOAP subscribe : add user failed")
                unless $list->add_list_member($u);
        }

        my $u = Sympa::User->new($sender);
        unless ($u->lang) {
            $u->lang($list->{'admin'}{'lang'});
            $u->save();
        }

        ## Now send the welcome file to the user
        unless ($action =~ /quiet/i) {
            unless ($list->send_probe_to_user('welcome', $sender)) {
                $log->syslog('err', 'Unable to send template "bye" to %s',
                    $sender);
            }
        }

        ## If requested send notification to owners
        if ($action =~ /notify/i) {
            $list->send_notify_to_owner(
                'notice',
                {   'who'     => $sender,
                    'gecos'   => $gecos,
                    'command' => 'subscribe'
                }
            );
        }
        $log->syslog('info', '%s from %s accepted', $listname, $sender);

        return SOAP::Data->name('result')->type('boolean')->value(1);
    }

    $log->syslog('info',
        '%s from %s aborted, unknown requested action in scenario',
        $listname, $sender);
    die SOAP::Fault->faultcode('Server')->faultstring('Undef')->faultdetail(
        "SOAP subscribe : %s from %s aborted because unknown requested action in scenario",
        $listname, $sender
    );
}

## Which list the user is subscribed to
## TODO (pour listmaster, toutes les listes)
sub complexWhich {
    my $self   = shift;
    my $sender = $ENV{'USER_EMAIL'};
    $log->syslog('notice', 'Xx complexWhich(%s)', $sender);

    $self->which('complex');
}

sub complexLists {
    my $self     = shift;
    my $topic    = shift || '';
    my $subtopic = shift || '';
    my $sender   = $ENV{'USER_EMAIL'};
    $log->syslog('notice', '(%s)', $sender);

    $self->lists($topic, $subtopic, 'complex');
}

## Which list the user is subscribed to
## TODO (pour listmaster, toutes les listes)
## Simplified return structure
sub which {
    my $self = shift;
    my $mode = shift;
    my @result;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    $log->syslog('notice', '(%s, %s)', $sender, $mode);

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    my %listnames;

    foreach my $role ('member', 'owner', 'editor') {
        foreach my $list (Sympa::List::get_which($sender, $robot, $role)) {
            my $name = $list->{'name'};
            $listnames{$name} = $list;
        }
    }

    foreach my $name (keys %listnames) {
        my $list = $listnames{$name};

        my $result_item;

        my $result = Sympa::Scenario::request_action(
            $list,
            'visibility',
            'md5',
            {   'sender'                  => $sender,
                'remote_application_name' => $ENV{'remote_application_name'}
            }
        );
        my $action;
        $action = $result->{'action'} if (ref($result) eq 'HASH');
        next unless ($action =~ /do_it/i);

        $result_item->{'listAddress'} =
            $name . '@' . $list->{'admin'}{'host'};
        $result_item->{'subject'} = $list->{'admin'}{'subject'};
        $result_item->{'subject'} =~ s/;/,/g;
        $result_item->{'homepage'} =
            Conf::get_robot_conf($robot, 'wwsympa_url') . '/info/' . $name;

        ## determine status of user
        $result_item->{'isOwner'} = 0;
        if ($list->is_admin('owner', $sender)
            or Sympa::is_listmaster($list, $sender)) {
            $result_item->{'isOwner'} = 1;
        }
        $result_item->{'isEditor'} = 0;
        if ($list->is_admin('actual_editor', $sender)) {
            $result_item->{'isEditor'} = 1;
        }
        $result_item->{'isSubscriber'} = 0;
        if ($list->is_list_member($sender)) {
            $result_item->{'isSubscriber'} = 1;
        }
        # determine bounce information of this user for this list
        if ($result_item->{'isSubscriber'}) {
            my $subscriber;
            if ($subscriber = $list->get_list_member($sender)) {
                $result_item->{'bounceCount'} = 0;
                if ($subscriber->{'bounce'} =~
                    /^(\d+)\s+(\d+)\s+(\d+)(\s+(.*))?$/) {
                    $result_item->{'firstBounceDate'} = $1;
                    $result_item->{'lastBounceDate'}  = $2;
                    $result_item->{'bounceCount'}     = $3;
                    if ($4 =~ /^\s*(\d+\.(\d+\.\d+))$/) {
                        $result_item->{'bounceCode'} = $1;
                    }
                }
                $result_item->{'bounceScore'} = $subscriber->{'bounce_score'};
            }
        }

        my $listInfo;
        if ($mode eq 'complex') {
            $listInfo = struct_to_soap($result_item);
        } else {
            $listInfo = struct_to_soap($result_item, 'as_string');
        }
        push @result, $listInfo;
    }

#    return SOAP::Data->name('return')->type->('ArrayOfString')
#    ->value(\@result);
    return SOAP::Data->name('return')->value(\@result);
}

sub getDetails {
    my $class    = shift;
    my $listname = shift;
    my $subscriber;
    my $list;
    my %result = ();

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list>');
    }

    $log->syslog('debug', 'SOAP getDetails(%s,%s,%s)',
        $listname, $robot, $sender);

    $list = Sympa::List->new($listname, $robot);
    if (!$list) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('List does not exist')->faultdetail('Use : <list>');
    }
    if ($subscriber = $list->get_list_member($sender)) {
        $result{'gecos'}         = $subscriber->{'gecos'};
        $result{'reception'}     = $subscriber->{'reception'};
        $result{'subscribeDate'} = $subscriber->{'date'};
        $result{'updateDate'}    = $subscriber->{'update_date'};
        $result{'custom'}        = [];
        if ($subscriber->{'custom_attribute'}) {
            foreach my $k (keys %{$subscriber->{'custom_attribute'}}) {
                push @{$result{'custom'}},
                    {
                    key   => $k,
                    value => $subscriber->{'custom_attribute'}{$k}{value}
                    }
                    if $k;
            }
        }
    } else {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Not a subscriber to this list')
            ->faultdetail('Use : <list>');
    }

    return SOAP::Data->name('return')->value(\%result);
}

sub setDetails {
    my $class     = shift;
    my $listname  = shift;
    my $gecos     = shift;
    my $reception = shift;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    my $subscriber;
    my $list;
    my %newcustom;
    my %user;

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail(
            'Use : <list> <gecos> <reception> [ <key> <value> ] ...');
    }

    $log->syslog('debug', 'SOAP setDetails(%s,%s,%s)',
        $listname, $robot, $sender);
    $list = Sympa::List->new($listname, $robot);
    if (!$list) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('List does not exist')
            ->faultdetail(
            'Use : <list> <gecos> <reception> [ <key> <value> ] ...');
    }
    $subscriber = $list->get_list_member($sender);
    if (!$subscriber) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Not a subscriber to this list')
            ->faultdetail(
            'Use : <list> <gecos> <reception> [ <key> <value> ] ...');
    }

    # Set subscriber values; return 1 for success.
    $user{gecos} = $gecos if defined $gecos and $gecos =~ /\S/;
    $user{reception} = $reception
        # ideally, this should check against the available_user_options
        # values from the $list config
        if $reception
            and $reception =~
            /^(mail|nomail|digest|digestplain|summary|notice|txt|html|urlize|not_me)$/;
    if (@_) {    # do we have any custom attributes passed?
        %newcustom = %{$subscriber->{'custom_attribute'}};
        while (@_) {
            my $key = shift;
            next unless $key;
            my $value = shift;
            if (!defined $value or $value eq '') {
                undef $newcustom{$key};
            } else {
                # $newcustom{$key} = $list->{'admin'}{'custom_attribute'}{$key}
                #     if !defined $newcustom{$key};
                $newcustom{$key}{value} = $value;
            }
        }
        $user{'custom_attribute'} = \%newcustom;
    }
    die SOAP::Fault->faultcode('Server')
        ->faultstring('Unable to set user details')
        ->faultdetail("SOAP setDetails : update user failed")
        unless $list->update_list_member($sender, %user);

    return SOAP::Data->name('result')->type('boolean')->value(1);
}

sub setCustom {
    my ($class, $listname, $key, $value) = @_;
    my $subscriber;
    my $list;
    my $rv;
    my %newcustom;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot  = $ENV{'SYMPA_ROBOT'};

    unless ($sender) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('User not authenticated')
            ->faultdetail('You should login first');
    }

    unless ($listname and $key) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Incorrect number of parameters')
            ->faultdetail('Use : <list> <key> <value>');
    }

    $log->syslog('debug', 'SOAP setCustom(%s,%s,%s,%s)',
        $listname, $robot, $sender, $key);

    $list = Sympa::List->new($listname, $robot);
    if (!$list) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('List does not exist')
            ->faultdetail('Use : <list> <key> <value>');
    }
    $subscriber = $list->get_list_member($sender);
    if (!$subscriber) {
        die SOAP::Fault->faultcode('Client')
            ->faultstring('Not a subscriber to this list')
            ->faultdetail('Use : <list> <key> <value> ');
    }
    %newcustom = %{$subscriber->{'custom_attribute'}};
    #if(! defined $list->{'admin'}{'custom_attribute'}{$key} ) {
    #	return SOAP::Data->name('result')->type('boolean')->value(0);
    #}
    if ($value eq '') {
        undef $newcustom{$key};
    } else {
        # $newcustom{$key} = $list->{'admin'}{'custom_attribute'}{$key}
        #     if !defined $newcustom{$key}
        #         and defined $list->{'admin'}{'custom_attribute'};
        $newcustom{$key}{value} = $value;
    }
    die SOAP::Fault->faultcode('Server')
        ->faultstring('Unable to set user attributes')
        ->faultdetail("SOAP setCustom : update user failed")
        unless $list->update_list_member($sender,
        custom_attribute => \%newcustom);

    return SOAP::Data->name('result')->type('boolean')->value(1);
}

## Return a structure in SOAP data format
## either flat (string) or structured (complexType)
sub struct_to_soap {
    my ($data, $format) = @_;
    my $soap_data;

    unless (ref($data) eq 'HASH') {
        return undef;
    }

    if ($format eq 'as_string') {
        my @all;
        my $formated_data;
        foreach my $k (keys %$data) {
			push @all, Encode::decode_utf8($k . '=' . $data->{$k});

        }

        $formated_data = join ';', @all;
        $soap_data = SOAP::Data->type('string')->value($formated_data);
    } else {
        my $formated_data;
        foreach my $k (keys %$data) {
            $formated_data->{$k} =
                SOAP::Data->name($k)->type($types{'listType'}{$k})
                ->value($data->{$k});
        }

        $soap_data = SOAP::Data->value($formated_data);
    }

    return $soap_data;
}

sub get_reason_string {
    my ($reason, $robot) = @_;

    my $data = {'reason' => $reason};
    my $string;

    my $template =
        Sympa::Template->new($robot, subdir => 'mail_tt2');    # FIXME: lang?
    unless ($template->parse($data, 'authorization_reject.tt2', \$string)) {
        my $error = $template->{last_error};
        $error = $error->as_string if ref $error;
        Sympa::send_notify_to_listmaster($robot, 'web_tt2_error', [$error]);
        $log->syslog('info', 'Error parsing');
        return '';
    }

    return $string;
}


###########################
## Patch NovaForge Start ## 
########################### 

## This subroutine allows to create a user in database
## Parameters are :
## 1. class
## 2. email
##Ã‚Â 3. gecos
## 4. passwd
## 5. lang

## returns OK if the user is successfully created
sub createUser {
    my ($class) = shift;
    my ($email) = shift;
    my ($gecos) = shift;
    my ($passwd) = shift;
    my ($lang) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog('info', 'SOAP createUser(email = %s,gecos = %s,passwd = %s,lang = %s) via proxy application %s', $email,$gecos,$passwd,$lang,$remote_application_name);

    unless ($email and $gecos and $passwd and $lang) {
	$log->syslog('err', 'createUser(): incorrect number of parameters');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters : Use <email> <gecos> <passwd> <lang>')
	    ->faultdetail('Use <email> <gecos> <passwd> <lang>');
    }

# check the validity of the email
    unless (Sympa::Tools::Text::valid_email($email)) {
	$log->syslog('err', 'createUser(): invalid email');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to add user : Invalid email')
	    ->faultdetail('Invalid email');
    }

# check the user doesn't already exists
    if (Sympa::User::is_global_user($email)) {
	$log->syslog('err', 'createUser(): a user with this email already exists');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to add user : Email already exists')
	    ->faultdetail('Email already exists');
    }

    unless (Sympa::User::add_global_user({'email' => $email,
				'password' => $passwd,
				'lang' => $lang,
				'gecos' => $gecos})) {
	$log->syslog('err', 'createUser(): add_global_user_failed');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to add user : add_global_user_failed')
	    ->faultdetail('add_global_user_failed');
    }

    return "OK";
}


## This subroutine allows to check if an user exists in SYMPA Organization
## Parameters are :
## 1. class
## 2. email

## returns OK if the user exists, KO if not exists
sub isUser {
    my ($class) = shift;
    my ($email) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog('info', 'SOAP isUser(email = %s) via proxy application %s', $email,$remote_application_name);

    unless ($email) {
	$log->syslog('err', 'isUser(): incorrect number of parameters');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters : Use <email> ')
	    ->faultdetail('Use <email>');
    }

# check the user doesn't already exists
    if (Sympa::User::is_global_user($email)) {
    	return "OK";
    }else{
    	return "KO";
    }
}


## This subroutine allows to delete a user in database and to unsubscribe it to all the lists he is subscribing
## Parameters are :
## 1. email
## ENV variables needed are : SYMPA_ROBOT the robot domain name

## returns OK if the user is successfully deleted
sub deleteUser {
    my ($class) = shift;
    my ($email) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};
    my $robot = $ENV{'SYMPA_ROBOT'};

    $log->syslog('info', 'SOAP deleteUser(email = %s) via proxy application %s', $email,$remote_application_name);

    unless ($email) {
	$log->syslog('err', 'deleteUser(): incorrect number of parameters');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters : Use  <email>')
	    ->faultdetail('Use : <email> ');
    }

# check the user exists
    unless (Sympa::User::is_global_user($email)) {
	$log->syslog('err', 'deleteUser(): no user found with this email');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to delete user : No user found with this email')
	    ->faultdetail('No user found with this email');
    }

    my (@users) = ($email);

# delete the user from the user table
    unless (Sympa::User::delete_global_user(@users)) {
	$log->syslog('err', 'deleteUser(): delete_global_user_failed for email = %s', $email);
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to delete user : delete_global_user_failed')
	    ->faultdetail('delete_global_user_failed');
    }

# delete the user from the lists he is subscriber
    my %listnames;
    
    foreach my $role ('member','owner','editor') {
	foreach my $list( Sympa::List::get_which($email,$robot,$role) ){    
	    my $name = $list->{'name'};
	    $listnames{$name} = $list;
	}
    }

   #=====================================================================================
   # In first step, we delete the user from list where he is subscriber 
   # if the user is a list owner,  it's complexe because Sympa allows to store 
   # owner in a static way (I.e config file) or a dynamic way in another file
   # which are defined by UI user ... 
   # We need to improve Sympa integration to control the delete process
   # multiple datasource. see later
   # nowadays, we launch a report to notice administrator to close user account if needed
   #======================================================================================
   my @userAdminError=();

    foreach my $name (keys %listnames) {
        my $list = $listnames{$name};
        my $listname = $list->{'name'};
        if ($list->is_list_member($email)) 
        { 
           $log->syslog('info', '%s Is subscriber of the list %s we delete this one',$email,$listname);
            &del($class,$listname,$email);
        } else { 
            $log->syslog('info', 'is not on the list %s',$listname);
            my ($is_admin, $count_admin)= Sympa::List::check_admin_to_delete($email,$name);
            if ($is_admin && $count_admin > 1) 
            {  
                #Ok here we can delete an admin (see later) 
                my $error= "The user defined by email : ". $email ." is owner of [". $listname ."] its possible to delete manually because an other owner exit on this list"; 
                push(@userAdminError,$error); 
            } else { 
                my $error= "The user defined by email : ". $email ." is owner of [". $listname ."] and it is the only"; 
                push (@userAdminError,$error ); 
                $log->syslog('info', 'Here the admin is alone we can not delete this one %s %s ',$email,$listname);
            }
        }

        if (scalar(@userAdminError) > 0) {    
            $log->syslog('err', 'deleteUser(): this user must be delete email = %s', $email);
            die SOAP::Fault->faultcode('100')
            ->faultstring(join (',', @userAdminError))
            ->faultdetail("") ;
        }
    }

    return "OK";
}


## This subroutine allows to delete a user on all the lists of a specified topic
## Parameters are :
## 1. email
## 2. topic name
## ENV variables needed are : SYMPA_ROBOT the robot domain name

## returns OK if the user is successfully deleted of all the lists of the topic
sub deleteUserOnTopic {
    my ($class) = shift;
    my ($email) = shift;
    my ($topicname) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};
    my $robot = $ENV{'SYMPA_ROBOT'};

    $log->syslog('info', 'SOAP deleteUserOnTopic(email = %s, topicname = %s) via proxy application %s', $email,$$topicname,$remote_application_name);

    unless ($email and $topicname) {
	$log->syslog('err', 'deleteUserOnTopic(): incorrect number of parameters');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters : Use <email> <topicname>')
	    ->faultdetail('Use : <email> <topicname> ');
    }

# check the user exists
    unless (Sympa::User::is_global_user($email)) {
	$log->syslog('err', 'deleteUserOnTopic(): no user found with this email');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to delete user : No user found with this email')
	    ->faultdetail('No user found with this email');
    }

    my (@users) = ($email);

# delete the user from the lists of the topic where he is subscriber
    my %listnames;
    
    foreach my $role ('member','owner','editor') {
	foreach my $list( Sympa::List::get_which($email,$robot,$role) ){    
	    my $name = $list->{'name'};
            $log->syslog('info', 'SOAP updateUserEmail(user is subscriber of listname = %s)',$name);
	    $listnames{$name} = $list;
	}
    }

    foreach my $name (keys %listnames) {
	    my $list = $listnames{$name};
            my $listname = $list->{'name'};
	    my @topics =   $list->{'admin'}{'topics'};
	    $log->syslog('info', 'deleteUserOnTopic(): topic= %s', $topics[0][0]);
	    if (@topics and $topics[0][0] eq $topicname) {
		 $log->syslog('info', 'deleteUserOnTopic(): delete the user on list = %s', $listname);
		 &del($class,$listname,$email);
	    }
    }

    return "OK";
}



## This subroutine allows to update a user in database except for its email (use updateUserEmail instead)
## Parameters are :
## 1. class
## 2. email
##Ã‚Â 3. gecos
## 4. passwd
## 5. lang

## returns OK if the user is successfully updated
sub updateUser {
    my ($class) = shift;
    my ($email) = shift;
    my ($gecos) = shift;
    my ($passwd) = shift;
    my ($lang) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};

    $log->syslog('info', 'SOAP updateUser(email = %s,gecos = %s,passwd = %s,lang = %s) via proxy application %s',$email,$gecos,$passwd,$lang,$remote_application_name);

    unless ($email and $gecos and $passwd and $lang) {
	$log->syslog('err', 'updateUser(): incorrect number of parameters');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters : Use <old_email> <email> <gecos> <passwd> <lang>')
	    ->faultdetail('Use : <old_email> <email> <gecos> <passwd> <lang>');
    }

# check the exists
    unless (Sympa::User::is_global_user($email)) {
	$log->syslog('err', 'updateUser(): no user found with this email');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to update user : No user found with this email')
	    ->faultdetail('No user found with this email');
    }

    unless (Sympa::User::update_global_user($email, {'gecos' => $gecos,'password' => $passwd,'lang' => $lang })) {
	$log->syslog('err', 'updateUser(): update_user_db_failed');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to update user : update_user_db_failed')
	    ->faultdetail('update_user_db_failed');
    }

    return "OK";
}

## This subroutine allows to update a user'email in database. It updates also all lists where user is subscriber
## Parameters are :
## 1. class
## 2. old_email
##Ã‚Â 3. new_email
## ENV variables needed are : SYMPA_ROBOT the robot domain name

## returns OK if the user is successfully updated
sub updateUserEmail {
    my ($class) = shift;
    my ($old_email) = shift;
    my ($new_email) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};
    my $robot = $ENV{'SYMPA_ROBOT'};

    $log->syslog('info', 'SOAP updateUserEmail(old_email = %s,email = %s) via proxy application %s',$old_email,$new_email,$remote_application_name);

    unless ($old_email and $new_email) {
	$log->syslog('err', 'updateUserEmail(): incorrect number of parameters');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters : Use <old_email> <email>')
	    ->faultdetail('Use : <old_email> <email> ');
    }

# check the user exists
    unless (Sympa::User::is_global_user($old_email)) {
	$log->syslog('err', 'updateUserEmail(): no user found with this email');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to update user email : No user found with this email')
	    ->faultdetail('No user found with this email');
    }

# check the validity of the email
    unless (Sympa::Tools::Text::valid_email($new_email)) {
	$log->syslog('err', 'updateUserEmail(): invalid email');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to update user : Invalid emai')
	    ->faultdetail('Invalid email');
    }

# check the new email is not used
    if (Sympa::User::is_global_user($new_email)) {
	$log->syslog('err', 'updateUserEmail(): an user with the new email has been found');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to update user email : An user with the new email has been found')
	    ->faultdetail('An user with the new email has been found');
    }

# loop on all the list where the user is subscriber to change the email_subscriber field
    my %listnames;
    
    foreach my $role ('member','owner','editor') {
	foreach my $list( Sympa::List::get_which($old_email,$robot,$role) ){    
	    my $name = $list->{'name'};
            $log->syslog('info', 'SOAP updateUserEmail(user is subscriber of listname = %s)',$name);
# if the role is owner, we need to update the config list file
	    $listnames{$name} = $list;
            if ($role eq 'owner'){
               	    unless ($list->update_owner_email($old_email,$new_email)) {	    
			$log->syslog('err', 'updateUserEmail(): update_owner_email_failed');
			die SOAP::Fault->faultcode('Client')
			    ->faultstring('Unable to update user email : update_owner_email_failed')
			    ->faultdetail('update_owner_email_failed');
	    	    }
            }		
	}
    }

    my $user = {};
    $user->{'email'} = $new_email;

    foreach my $name (keys %listnames) {
	    my $list = $listnames{$name};

	    unless ($list->update_list_member($old_email,$user)) {	    	    
		$log->syslog('err', 'updateUserEmail(): update_user_failed');
		die SOAP::Fault->faultcode('Client')
		    ->faultstring('Unable to update user : update_user_failed')
		    ->faultdetail('update_user_failed');
	    }
    }

# update the email field in the user table
    unless (Sympa::User::update_global_user($old_email, {'email' => $new_email})) {
	$log->syslog('err', 'updateUserEmail(): update_user_db_failed');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to update user : update_user_db_failed')
	    ->faultdetail('update_user_db_failed');
    }
    
    return "OK";
}

## This subroutine allows to create a new subtopic. it creates the categorytopic if it doesn't exists
## Parameters are :
## 1. class
## 2. name --- the id of the topic
##Ã‚Â 3. category --- the category of the topic (ie the parent topic title-- the parent topic id is extracted from subtopic id)
##Ã‚Â 4. title --- the title of the topic
##Ã‚Â 5. visibility --- the visibility of the topic and of the subtopic (the same)

## ENV variables needed are : SYMPA_ROBOT the robot domain name

## returns OK if the user is successfully updated
sub createTopic {
    my ($class) = shift;
    my ($name) = shift;
    my ($visibility) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};
    my $robot = $ENV{'SYMPA_ROBOT'};

    $log->syslog('info', 'SOAP createTopic(name = %s) via proxy application %s',$name,$remote_application_name);

# create the topic if it does not exist
    my %topics;
    unless (%topics = Sympa::Robot::load_topics($robot)) {
	$log->syslog('err', 'createTopic(): load_topics_failed');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to create topic : load_topics_failed')
	    ->faultdetail('load_topics_failed');
    }

    my $parentExists = 'false';
    foreach my $topic_name (keys %topics) {
	if ($topic_name eq $name){
		$parentExists = 'true';
	}
    }

    unless ($parentExists eq "true") {
	    unless (Sympa::List::create_topic($robot,$name,$name,$visibility)) {
		die SOAP::Fault->faultcode('Client')
		    ->faultstring('Unable to create parent topic : create_topic_failed')
		    ->faultdetail('create_topic_failed');
	    }
    }

    return "OK";
}

## This subroutine allows to delete a subtopic, delete all the lists linked to the subtopic and the topic parent if no more subtopics exists
## Parameters are :
## 1. class
## 2. name --- the id of the topic
## ENV variables needed are : SYMPA_ROBOT the robot domain name

## returns OK if the subtopic is successfully deleted
sub deleteTopic {
    my ($class) = shift;
    my ($name) = shift;

    my $remote_application_name = $ENV{'remote_application_name'};
    my $robot = $ENV{'SYMPA_ROBOT'};

    $log->syslog('info', 'SOAP deleteTopic(name = %s) via proxy application %s',$name,$remote_application_name);

# check if it's a topic or a subtopic
    my @tab = split '/', $name;
    my $isSubTopic = 'false';
    if (scalar @tab == 2){
	$isSubTopic = 'true';
    }

    if ($isSubTopic eq "true"){
        # check the subtopic exists
        my $ret = Sympa::List::exist_subtopic($robot, $name);
        unless ( $ret and $ret eq "true"){
	$log->syslog('err', 'deleteTopic(): subtopic does not exists');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to delete subtopic : Subtopic does not exists')
	    ->faultdetail('Subtopic does not exists');
        }
    }
    elsif ($isSubTopic eq "false"){
       # check the topic exists
       my $ret = Sympa::List::exist_topic($robot, $name);
       unless ( $ret and $ret eq "true"){
       $log->syslog('err', 'deleteTopic(): topic does not exists');
       die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to delete topic : Topic does not exists')
	    ->faultdetail('Topic does not exists');
       }
    }

# delete the topic in the topics.conf file
    unless (Sympa::List::delete_topic($robot,$name)) {
	$log->syslog('err', 'deleteTopic(): delete_topic_failed');
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Unable to delete topic : delete_topic_failed')
	    ->faultdetail('delete_topic_failed');
    }

# delete all the lists linked to the topic
    my $all_lists = Sympa::List::get_lists($robot);
    foreach my $list ( @$all_lists ) {
	my $listname = $list->{'name'};
        my @topics =   $list->{'admin'}{'topics'};
	if (@topics and $topics[0][0] eq $name) {
		$log->syslog('info', 'deleteTopic(): close the list = %s', $listname);
		&closeList($class,$listname);
	}
    }

    return "OK";
}

## This subroutine allows to add many subscribers to a list
## Parameters are :
## 1. class
## 2. listname --- the name of the list
## 3. emails   --- the CSV comma separated value of the emails to add
## 4. quiet    --- true : no notification is sent to the new subscribers/ false : a notification is sent to the new subscribers
## ENV variables needed are : SYMPA_ROBOT the robot domain name
sub addSubscribers {
    my $class = shift;
    my $listname  = shift;
    my $emails = shift;
    my $quiet = shift;

    my $sender = $ENV{'USER_EMAIL'};
    my $robot = $ENV{'SYMPA_ROBOT'};
    my $remote_application_name = $ENV{'remote_application_name'};
    
    $log->syslog('info', 'SOAP addSubscribers(list = %s@%s,emails = %s,quiet = %s) from %s via proxy application %s', $listname,$robot,$emails,$quiet,$sender,$remote_application_name);

    unless ($sender) {
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('User not specified')
	    ->faultdetail('Use a trusted proxy or login first ');
    }
    
    unless ($listname) {
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters')
	    ->faultdetail('Use : <list>');
    }
    unless ($emails) {
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Incorrect number of parameters')
	    ->faultdetail('Use : <email>');
    }
	my $list = Sympa::List->new($listname, $robot);
    unless ($list) {
	$log->syslog('info', 'add %s@%s %s from %s refused, no such list ', $listname,$robot,$emails,$sender);
	die SOAP::Fault->faultcode('Server')
	    ->faultstring('Undefined list')
	    ->faultdetail("Undefined list");
    }

    # check authorization				  
    my $result = Sympa::Scenario::request_action(
        $list, 'add', 'md5',
        {   'sender'                  => $sender,
            'email'                   => $emails,
            'remote_host'             => $ENV{'REMOTE_HOST'},
            'remote_addr'             => $ENV{'REMOTE_ADDR'},
            'remote_application_name' => $ENV{'remote_application_name'}
        }
    );

    
    my $action;
    my $reason;
    if (ref($result) eq 'HASH') {
	$action = $result->{'action'};
	$reason = $result->{'reason'};
    }

    unless (defined $action){
	$log->syslog('info', 'addSubscribers %s@%s %s from %s : scenario error', $listname,$robot,$emails,$sender);
	die SOAP::Fault->faultcode('Server')
	    ->faultstring('scenario error')
	    ->faultdetail("sender $sender email $emails remote $ENV{'remote_application_name'} ");
    }

    unless ($action =~ /do_it/) {
	my $reason_string = &get_reason_string($reason,$robot);
	$log->syslog('info', 'SOAP : addSubscribers %s@%s %s from %s refused (not allowed)',  $listname,$robot,$emails,$sender);
	die SOAP::Fault->faultcode('Client')
	    ->faultstring('Not allowed')
	    ->faultdetail($reason_string);
    }


##### loop process on each email of the tab

    my @values = split(';', $emails);
    my @emails_errors_returned;

    foreach my $email (@values) {
            my $gecos = "";

            # check the validity of the email
            unless (Sympa::Tools::Text::valid_email($email)) {
	       $log->syslog('err', 'addSubscribers %s: invalid email', $email);
               push @emails_errors_returned, $email;
               next;
            }


            $log->syslog('info', 'SOAP : addSubscribers %s on subscribers list for %s mailing-list',  $email,$listname);

	    if ($list->is_list_member($email)) {
	      $log->syslog('err', 'addSubscribers %s@%s %s from %s : failed, user already member of the list', $listname,$robot,$email,$sender);
              push @emails_errors_returned, $email;
              next;
	    }
		
            my $u;
	    my $defaults = $list->get_default_user_options();
	    my $u2 = Sympa::User::get_global_user($email);
	    %{$u} = %{$defaults};
	    $u->{'email'} = $email;
	    $u->{'gecos'} = $gecos || $u2->{'gecos'};
	    $u->{'date'} = $u->{'update_date'} = time;
	    $u->{'password'} = $u2->{'password'} || Sympa::Tools::Password::tmp_passwd($email) ;
	    $u->{'lang'} = $u2->{'lang'} || $list->{'admin'}{'lang'};
	
	    unless ($list->add_list_member($u)) {
		$log->syslog('err', 'addSubscribers %s@%s %s from %s : Unable to add user', $listname,$robot,$email,$sender);
                push @emails_errors_returned, $email;
                next;
	    }
	    #$list->delete_subscription_request($email);
        my $spool_req = Sympa::Spool::Auth->new(
            context => $list,
            email   => $email,
            action  => 'add'
        );
        while (1) {
            my ($request, $handle) = $spool_req->next;
            last unless $handle;
            next unless $request;

            $spool_req->remove($handle);
        }
	    	    
	    ## Now send the welcome file to the user if it exists and notification is supposed to be sent.
	    unless ($quiet || $action =~ /quiet/i) {
		unless ($list->send_file('welcome', $email, $robot,{'auto_submitted' => 'auto-generated'})) {
		    $log->syslog('notice',"Unable to send template 'welcome' to $email");
		}
	    }
	    
	    $log->syslog('info', 'ADD %s %s from %s accepted (%d subscribers)', $list->{'name'}, $email, $sender, $list->get_total() );
	    if ($action =~ /notify/i) {
		unless ($list->send_notify_to_owner('notice',{'who' => $email, 
							      'gecos' => $gecos,
							      'command' => 'add',
							      'by' => $sender})) {
		    $log->syslog('info',"Unable to send notify 'notice' to $list->{'name'} list owner");
		}
	    }
    }
    
    return SOAP::Data->name('return')->value(\@emails_errors_returned);
}

######################### 
## Patch NovaForge End ## 
######################### 

1;

