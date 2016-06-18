<?php
	// PHP Compat stuff
	if (!function_exists('json_encode')) {
		require_once 'Json.php';
		function json_encode($arg)
		{
				global $services_json;
				if (!isset($services_json)) {
						$services_json = new Services_JSON();
				}
				return $services_json->encode($arg);
		}
	} 

	if( !function_exists('json_decode') ) {
		require_once 'Json.php';
		function json_decode($data, $bool) {
			if ($bool) {
				$json = new Services_JSON(SERVICES_JSON_LOOSE_TYPE);
			} else {
				$json = new Services_JSON();
			}
			return( $json->decode($data) );
		}
	}
?>