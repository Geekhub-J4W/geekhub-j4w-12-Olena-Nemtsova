package edu.geekhub.homework;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.tinylog.Logger;

@Controller
public class ErrorControllerIml implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        var exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Logger.warn(exception);

        String message = "";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null
            && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            message = "page not found";
        }

        model.addAttribute("message", message);
        return "error";
    }
}
