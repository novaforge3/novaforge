#!/usr/bin/perl
###################################################################################
# Re-Index.pl - Starts re-index job for jira. 
###################################################################################
 
#disable ssl verfication, using this for self-signed cert otherwise comment out.
$ENV{'PERL_LWP_SSL_VERIFY_HOSTNAME'} = 0;
use LWP::UserAgent;
# Change these
my $user = "technicalAdminUsername";
my $pass = "technicalAdminPassword";
my $jira_url = "http://localhost:8080aliasJira/"; 
my $cookie_jar = $ENV{'HOME'}; #change this if you need to, should only be needed if there is a permissions issue.
my $index_type = "stoptheworld"; #This is for jira 5.2 and above. Set to 1 for background re-index or 0 for traditional.
 
 
#setup initial connection paramaters
my $status;
print("Creating connection to [$jira_url]... \n");
my $ua = LWP::UserAgent->new;
$ua->cookie_jar({ file => "$cookie_jar/.cookies.txt" });
$ua->default_header('X-Atlassian-Token' => 'no-check');
 
 
#do login
$status = $ua->post($jira_url.'secure/AdminSummary.jspa', [ 'os_username'   => $user, 'os_password'  => $pass]);
if($status->header('X-Seraph-LoginReason') eq "AUTHENTICATED_FAILED" || $status->code !=200) {die("Could not login to jira, verify username and password!\n");}
 
print("Successfully logged in to Jira.\n");
#do websudo
$status = $ua->post($jira_url.'secure/admin/WebSudoAuthenticate.jspa',[ 'webSudoPassword'   => $pass]);
 
# I'm using the http code here because the user header check stays ok and I didn't want to grep the output for an error... 
# If we pass sudo check we get redirected to /secure/ and code is 302, otherwise we get served an error page with status 200
if($status->code !=302) { 
    unlink('$cookie_jar/.cookies.txt');
    die("We did not sudo properly, check that your password is good and that your user is an admin!\n");
}
 
print("Successfully passed websudo, kicking off indexing... ");
#do re-index
if($index_type == 1) {$index_type='background';}
$status = $ua->post($jira_url.'IndexReIndex.jspa', [ 'indexPathOption' => 'DEFAULT','Re-Index' =>'Re-Index', 'indexingStrategy' => $index_type]);
if($status->code != 302) { 
    print("Could not start re-index, check that your password is good and that your user is an admin!\n");
} else {
    print("Re-index has started.\n");
}
unlink('$cookie_jar/.cookies.txt');
