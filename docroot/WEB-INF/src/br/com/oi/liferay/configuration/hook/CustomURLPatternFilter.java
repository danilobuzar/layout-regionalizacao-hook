package br.com.oi.liferay.configuration.hook;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

public class CustomURLPatternFilter implements Filter {

	private static final Log LOG = LogFactoryUtil.getLog(CustomURLPatternFilter.class);

	@Override
	public void destroy() {
		LOG.info("#####CustomURLPatternFilter.destroy()");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;

		final String requestURI = request.getRequestURI();
		try {
			final String[] urlPaths = StringUtil.split(requestURI, StringPool.FORWARD_SLASH);

			LOG.info("URL paths (" + urlPaths.length + "): ");

			for (final String s : urlPaths) {
				LOG.info(s + "\n");
			}

			if (urlPaths.length == 5) {

				final String estado = urlPaths[3].replace("_", "");
				final String cidade = urlPaths[4].replace("_", "");

				LOG.info("Parametro Estado: " + estado);
				LOG.info("Parametro Cidade: " + cidade);

				final String regiao = getRegion(estado, cidade);

				LOG.info("Regiao encontrada " + regiao);

				final String forwardPath = "/web/internet/" + regiao;

				req.getRequestDispatcher(forwardPath).forward(req, res);

				chain.doFilter(req, res);
			} else {
				chain.doFilter(req, res);
			}
		} catch (final Exception e) {
			chain.doFilter(req, res);
			LOG.info(e.getMessage());
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		LOG.info("Chamado CustomFilter.init(" + arg0 + ")");
	}

	private String getRegion(String estado, String cidade) {
		String retValue = null;

		if ("rio-de-janeiro".equals(estado) && "rj".equals(cidade)) {
			retValue = "rg-2";
		} else {
			retValue = "ca-2";
		}

		return retValue;
	}

}
