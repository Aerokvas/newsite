package artas.newsite.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("error", status + " - " + message);
        model.addAttribute("status", status);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", status
                        + " - страница не найдена.");
                model.addAttribute("status", status);
            }

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", status
                        + " - тебе сюда нельзя, сталкер.");
                model.addAttribute("status", status);
            }

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", status
                        + " - что-то пошло не так, обратитесь к владельцу.");
                model.addAttribute("status", status);
            }
        }

        return "error";
    }
}

