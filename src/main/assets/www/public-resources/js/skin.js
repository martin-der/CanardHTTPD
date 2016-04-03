

jQuery(document).ready(function () {


	var theme = $.cookie('theme');

	if ( theme != undefined ) {
		if (options.setThemeClientSide) {
			setTheme ( theme );
		}
		// workaround stupidity of the server that can select the
		// right theme option while generating the page
		$("select#theme>option[value='"+theme+"']").prop('selected', true)
	}

	function setTheme ( theme) {
		
		var resetCss = "<script id='reinit_css' type='text/css'> * { color : initial; }</script>";
		$("html>head>script[id='reinit_css']").remove();

		var commonCssLink = "<link id='common_css' rel='stylesheet' href='/~/css/common.css' type='text/css'>";
		$("html>head>link[id='common_css']").remove();

		var newCssLink = "<link id='global_css' rel='stylesheet' href='/~/theme/"+theme+"/main.css' type='text/css'>";
		$("html>head>link[id='global_css']").remove();

		var mandatoryCssLink = "<link id='mandatory_css' rel='stylesheet' href='/~/css/mandatory.css' type='text/css'>";
		$("html>head>link[id='mandatory_css']").remove();


		$("html>head").append(resetCss + commonCssLink + newCssLink + mandatoryCssLink);
	}


	jQuery('select#theme').on('change', function (e) {
		var option = $('option:selected', this);
		var theme = this.value;
		
		$.blockUI({ message: '<h1><img src="/~/image/busy.gif" />'+message.theme_loading+'</h1>' });
		try {
			setTheme ( theme );
			$.cookie('theme', theme);
		} finally {
			$.unblockUI();
		}

	});


});