package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String API_PATH = "/api/posts";
    private static final String DIGITAL_PATTERN = "/\\d";
    private static final String SLASH = "/";


    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            String path = req.getRequestURI();
            String method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && path.equals(API_PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(API_PATH + DIGITAL_PATTERN)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(SLASH)));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(API_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(API_PATH + DIGITAL_PATTERN)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(SLASH)));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}