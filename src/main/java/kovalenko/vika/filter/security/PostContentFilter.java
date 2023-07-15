package kovalenko.vika.filter.security;

import kovalenko.vika.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Enumeration;

import static kovalenko.vika.utils.constants.LinkConstant.COMMENT_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.NEW_TASK_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.REGISTER_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TAG_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebFilter(filterName = "PostRequestContentFilter",
        urlPatterns = {REGISTER_LINK,
                NEW_TASK_LINK,
                TODO_LINK,
                TAG_LINK,
                COMMENT_LINK})
public class PostContentFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("PostRequestContentFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (ServletUtil.isPostRequest(request)) {
            Enumeration<String> params = request.getParameterNames();

            while (params.hasMoreElements()) {
                String paramName = params.nextElement();
                String paramContent = request.getParameter(paramName);

                if (containsHtml(paramContent)) {
                    log.warn("The request parameter '{}' contains html-elements: '{}'", paramName, paramContent);
                    request.setAttribute(paramName, replaceHtmlTagsChars(paramContent));
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean containsHtml(String target) {
        return !Jsoup.isValid(target, Safelist.none());
    }

    private String replaceHtmlTagsChars(String content) {
        return content.replace("<", "&#60;")
                .replace(">", "&#62;");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.debug("PostRequestContentFilter destroyed");
    }
}
