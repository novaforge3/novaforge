<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * @copyright Copyright 2016  Atos, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */

function mc_project_get_issues_history_by_status( $p_username, $p_password, $p_project_id, $p_start, $p_end, $p_incr,  $p_filter ) {
	$t_user_id = mci_check_login( $p_username, $p_password );
	if( $t_user_id === false ) {
		return mci_soap_fault_login_failed();
	}

	$t_lang = mci_get_user_lang( $t_user_id );

	if( $p_project_id != ALL_PROJECTS && !project_exists( $p_project_id ) ) {
		return new soap_fault( 'Client', '', "Project '$p_project_id' does not exist." );
	}

	if( !mci_has_readonly_access( $t_user_id, $p_project_id ) ) {
		return mci_soap_fault_access_denied( $t_user_id );
	}

	$t_page_number = 1;

	$t_per_page = -1;
	$t_page_count = -1;
	$t_bug_count = -1;
	$t_filter = array();	
	if (isset($p_filter)){
		foreach ($p_filter as $p_filter_data) {
			$t_filter[$p_filter_data->name] = $p_filter_data->filter_string;
		}
	}

	$t_filter['_view_type']	= 'advanced';
	$t_filter[FILTER_PROPERTY_STATUS] = array(META_FILTER_ANY);

	$rows = filter_get_bug_rows( $t_page_number, $t_per_page, $t_page_count, $t_bug_count, $t_filter, $p_project_id );

	$t_bug_table			= db_get_table( 'bug' );
	$t_bug_hist_table		= db_get_table( 'bug_history' );

	$t_marker = array();
	$t_data = array();
	$t_ptr = 0;
	$t_end = get_end_timestamp($p_end);
	$t_start = get_start_timestamp($p_start);
	$t_incr = get_incr($t_end, $t_start);

	// grab all status levels
	$t_status_arr  = get_statuses_enum( $p_project_id );
	$t_default_bug_status = config_get( 'bug_submit_status' );

	$t_bug = array();
	$t_view_status = array();

	// walk through all issues and grab their status for 'now'
	$t_marker[$t_ptr] = time();
	foreach ($rows as $t_row) {
		if ( isset( $t_data[$t_ptr][$t_row->status] ) ) {
			$t_data[$t_ptr][$t_row->status] ++;
		} else {
			$t_data[$t_ptr][$t_row->status] = 1;
			$t_view_status[$t_row->status] =
			isset($t_status_arr[$t_row->status]) ? $t_status_arr[$t_row->status] : '@'.$t_row->status.'@';
		}
		$t_bug[] = $t_row->id;
	}

	if (!$t_bug) {
		return;	
	}

	// get the history for these bugs over the interval required to offset the data
	// type = 0 and field=status are status changes
	// type = 1 are new bugs
	db_param_push();
	$t_query = 'SELECT bug_id, type, old_value, new_value, date_modified FROM {bug_history} WHERE bug_id in( '. db_param() .') and ( (type='.NORMAL_TYPE.' and field_name=\'status\') or type='.NEW_BUG.' ) and (date_modified BETWEEN '. db_param() .' and '. db_param() .') order by date_modified DESC';
	$t_result = db_query( $t_query, Array(implode(',', $t_bug),$t_start, $t_end ));
	
	$t_row = db_fetch_array( $t_result );

	for ($t_now = $t_end - $t_incr; $t_now >= $t_start; $t_now -= $t_incr) {
		// walk through the data points and use the data retrieved to update counts
		while( ( $t_row !== false ) && ( $t_row['date_modified'] >= $t_now ) ) {
			switch ($t_row['type']) {
				case 0: // updated bug
					if ( isset( $t_data[$t_ptr][$t_row['new_value']] ) ) {
						if ( $t_data[$t_ptr][$t_row['new_value']] > 0 )
							$t_data[$t_ptr][$t_row['new_value']] --;
					} else {
						$t_data[$t_ptr][$t_row['new_value']] = 0;
						$t_view_status[$t_row['new_value']] =
						isset($t_status_arr[$t_row['new_value']]) ? $t_status_arr[$t_row['new_value']] : '@'.$t_row['new_value'].'@';
					}
					if ( isset( $t_data[$t_ptr][$t_row['old_value']] ) ) {
						$t_data[$t_ptr][$t_row['old_value']] ++;
					} else {
						$t_data[$t_ptr][$t_row['old_value']] = 1;
						$t_view_status[$t_row['old_value']] =
						isset($t_status_arr[$t_row['old_value']]) ? $t_status_arr[$t_row['old_value']] : '@'.$t_row['old_value'].'@';
					}
					break;
				case 1: // new bug
					if ( isset( $t_data[$t_ptr][$t_default_bug_status] ) ) {
						if ( $t_data[$t_ptr][$t_default_bug_status] > 0 )
							$t_data[$t_ptr][$t_default_bug_status] --;
					} else {
						$t_data[$t_ptr][$t_default_bug_status] = 0;
						$t_view_status[$t_default_bug_status] =
						isset( $t_status_arr[$t_default_bug_status] ) ? $t_status_arr[$t_default_bug_status] : '@' . $t_default_bug_status . '@';
					}
					break;
			}
			$t_row = db_fetch_array( $t_result );
		}

		if ($t_now <= $t_end) {
			$t_ptr++;
			$t_marker[$t_ptr] = $t_now;
			foreach ( $t_view_status as $t_status => $t_label ) {
				$t_data[$t_ptr][$t_status] = $t_data[$t_ptr-1][$t_status];
			}
		}
	}
	ksort($t_view_status);
	$t_bin_count = $t_ptr;

	// reverse the array and consolidate the data, if necessary
	$t_metrics = array();
	$t_metrics_status = array();
	for ($t_ptr=0; $t_ptr<$t_bin_count; $t_ptr++) {
		$t = $t_bin_count - $t_ptr;
		$t_metrics[0][$t_ptr] = $t_marker[$t];
		$i = 0;
		foreach ( $t_view_status as $t_status => $t_label ) {
			if ( isset( $t_data[$t][$t_status] ) ){
				$t_metrics[++$i][$t_ptr] = $t_data[$t][$t_status];
			}
			else
			{
				$t_metrics[++$i][$t_ptr] = 0;
			}
			$t_metrics_status[$t_status][$t_ptr] = array(
					'time' => SoapObjectsFactory::newDateTimeString( $t_metrics[0][$t_ptr], false ),
					'value' => $t_metrics[$i][$t_ptr],
			);
		}
	}
	$t_result = array();
	foreach ( $t_view_status as $t_status => $t_label ) {
		$t_status_result = array();
		$t_status_result['status'] = $t_status;
		$t_status_result['history'] = $t_metrics_status[$t_status];
		$t_result[] = $t_status_result;
	}
	return $t_result;
}

/**
 * get start date in unix timestamp format
 */
function get_start_timestamp( $p_start ) {
	if( $p_start == '' ) {
		$t_today = date( 'Y-m-d' );
		list( $t_year, $t_month, $t_day ) = explode( "-", $t_today );
		$t_start = strftime( '%Y-%m-%d 00:00:00', mktime( 0, 0, 0, 1, 1, $t_year ) );
	} else {
		$t_start = strftime( '%Y-%m-%d', strtotime( $p_start ) ) . ' 00:00:00';
	}
	return strtotime($t_start);
}

/**
 * Get end date in unix timestamp format
 */
function get_end_timestamp($p_end) {
	$t_end = date( 'Y-m-d' ) . ' 23:59:59';
	if( $p_end != '' ) {
		$t_end = strftime( '%Y-%m-%d', strtotime( $p_end ) ) . ' 23:59:59';
	}
	return strtotime($t_end);
}

/**
 * Get increment depending on periode
 */
function get_incr($p_end_timestamp, $p_start_timestamp) {
		$t_elapsed_days = ( $p_end_timestamp - $p_start_timestamp ) / ( 24 * 60 * 60 );
		if ( $t_elapsed_days <= 7 ) {
	    $t_incr = 60 * 60; // less than 7 days, use hourly
	    } else if ( $t_elapsed_days <= 184 ) {
			$t_incr = 24 * 60 * 60; // less than six months, use daily
		} else {
			$t_incr = 7 * 24 * 60 * 60; // otherwise weekly
		}
	return $t_incr;
}

function get_statuses_enum( $p_project_id ){

	# if config_status_id.php is present load the custom status, else load the default one
	if ( file_exists( dirname( dirname( dirname( __FILE__ ) ) ).DIRECTORY_SEPARATOR.'conf-instance'.DIRECTORY_SEPARATOR.'config_status_'.$p_project_id.'.php' ) ) {
		require_once( dirname( dirname( dirname( __FILE__ ) ) ).DIRECTORY_SEPARATOR.'conf-instance'.DIRECTORY_SEPARATOR.'config_status_'.$p_project_id.'.php' );
		if (isset($g_status_enum_string)){
			$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( $g_status_enum_string );
		} else {
			$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
		}
	} else {
		$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	}

	return $t_enum_status;
}

