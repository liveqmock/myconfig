package net.myconfig.web.renderer;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface HttpRenderer<T> {

	void renderer(T set, HttpServletResponse response) throws IOException;

}
