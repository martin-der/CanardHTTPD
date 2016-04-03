package net.tetrakoopa.canardhttpd.service.http.writer.template;


public class TemplateParam {

	public static final TemplateArg INDEX_TEMPLATE_PARAMS = new TemplateArg();
	public static final TemplateArg SHARE_DETAIL_TEMPLATE_PARAMS = new TemplateArg();

	static {
		INDEX_TEMPLATE_PARAMS.page_title = "Index";

		SHARE_DETAIL_TEMPLATE_PARAMS.page_title = "Detail";
	}
}
