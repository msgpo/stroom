package stroom.core.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.config.common.ApiGatewayConfig;
import stroom.ui.config.shared.UiConfig;
import stroom.util.io.CloseableUtil;
import stroom.util.io.StreamUtil;
import stroom.util.shared.IsServlet;
import stroom.util.shared.ResourcePaths;
import stroom.util.shared.Unauthenticated;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * The swagger specs are generated by the gradle task
 * ./gradlew :stroom-app:generateSwaggerDocumentation
 * which places the swagger.json file in
 * stroom-app/src/main/resources/ui/noauth/swagger/
 */
@Unauthenticated
public class SwaggerUiServlet extends HttpServlet implements IsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerUiServlet.class);

    private static final Set<String> PATH_SPECS = Set.of("/swagger-ui");

    private static final String TITLE = "@TITLE@";
    private static final String SWAGGER_SPEC_URL_TAG = "@SWAGGER_SPEC_URL@";
    private static final String SWAGGER_UI_REL_DIR_TAG = "@SWAGGER_UI_REL_DIR@";

    private final ApiGatewayConfig apiGatewayConfig;
    private final UiConfig uiConfig;

    private String template;

    @Inject
    public SwaggerUiServlet(final ApiGatewayConfig apiGatewayConfig,
                            final UiConfig uiConfig) {
        this.apiGatewayConfig = apiGatewayConfig;
        this.uiConfig = uiConfig;
    }

    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {
        try (final PrintWriter printWriter = response.getWriter()) {
            response.setContentType("text/html");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(200);

            String html = getHtmlTemplate();
            html = html.replace(TITLE, uiConfig.getHtmlTitle());
            html = html.replace(SWAGGER_SPEC_URL_TAG, getSwaggerSpecUrl());
            html = html.replace(SWAGGER_UI_REL_DIR_TAG, "swagger-ui-dist");

            printWriter.write(html);
        } catch (final IOException e) {
            LOGGER.error("Error retrieving Swagger UI", e);
            throw new ServletException("Error retrieving stroom status");
        }
    }

    @Override
    public void init() throws ServletException {
        LOGGER.info("Initialising Swagger UI");
        super.init();
        LOGGER.info("Initialised Swagger UI");
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroying Swagger UI");
        super.destroy();
        LOGGER.info("Destroyed Swagger UI");
    }

    @Override
    public Set<String> getPathSpecs() {
        return PATH_SPECS;
    }

    private String getHtmlTemplate() {
        if (template == null) {
            final InputStream is = getClass().getResourceAsStream("swagger-ui-template.html");
            template = StreamUtil.streamToString(is);
            CloseableUtil.closeLogAndIgnoreException(is);
        }
        return template;
    }

    private String getSwaggerSpecUrl() {
        return apiGatewayConfig.buildApiGatewayUrl(
            ResourcePaths.buildUnauthenticatedServletPath("swagger/swagger.json"));
    }

}
