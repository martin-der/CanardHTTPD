
STR_loading_theme="Loading theme '{THEME}'...";

jQuery(document).ready(function () {
	
	var pref_theme = $.cookie('theme');
	
	if ( pref_theme != undefined ) {
		setTheme ( pref_theme );
	}

	function setTheme ( theme) {
		
		var resetCss = "<script id='reinit_css' type='text/css'> * { color : initial; }</script>";
		$("html>head>script[id='reinit_css']").remove();

		var commonCssLink = "<link id='common_css' rel='stylesheet' href='./RES/css/common.css' type='text/css'>";
		$("html>head>link[id='common_css']").remove();

		var newCssLink = "<link id='global_css' rel='stylesheet' href='./RES/theme/"+theme+"/main.css' type='text/css'>";
		$("html>head>link[id='global_css']").remove();

		var mandatoryCssLink = "<link id='mandatory_css' rel='stylesheet' href='./RES/css/mandatory.css' type='text/css'>";
		$("html>head>link[id='mandatory_css']").remove();


		$("html>head").append(resetCss + commonCssLink + newCssLink + mandatoryCssLink);
	}


	jQuery('select#site_theme').on('change', function (e) {
		var option = $('option:selected', this);
		var theme = this.value;
		
		$.blockUI({ message: '<h1><img src="./RES/image/busy.gif" />'+STR_loading_theme+'</h1>' });
		try {
			setTheme ( theme );
			$.cookie('theme', theme);
		} finally {
			$.unblockUI();
		}

	});


});