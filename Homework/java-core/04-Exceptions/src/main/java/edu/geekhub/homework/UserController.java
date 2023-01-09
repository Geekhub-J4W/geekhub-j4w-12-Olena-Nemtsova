package edu.geekhub.homework;

import edu.geekhub.controller.Controller;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;
import edu.geekhub.models.User;


// Don't move this class
public class UserController implements Controller {
    private final UserService service;

    public UserController() {
        this.service = new UserService();
    }

    @Override
    public Response process(Request request) {
        String fullExceptionMessage = Validator.validateUser(request.getData(), service);

        if (fullExceptionMessage.isBlank()) {
            User user = (User) request.getData();
            service.addUser(user);
            return Response.ok(user);
        } else {
            return Response.fail(fullExceptionMessage + "\n" + request.getData());
        }
    }
}
