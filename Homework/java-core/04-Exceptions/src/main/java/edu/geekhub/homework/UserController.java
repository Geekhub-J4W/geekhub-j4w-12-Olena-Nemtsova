package edu.geekhub.homework;

import edu.geekhub.controller.Controller;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;
import edu.geekhub.models.User;

import java.util.ArrayList;
import java.util.List;

// Don't move this class
public class UserController implements Controller {

    private final UserService service = new UserService();
    private final List<UserDataFailedException> exceptionList = new ArrayList<>();
    private User[] users = new User[0];
    private User user;

    public UserController() {
        setExceptionList();
    }

    @Override
    public Response process(Request request) {

        users = service.getUsers();

        String fullExceptionMessage = "";
        try {
            user = (User) request.getData();
            if (user == null) throw exceptionList.get(0);

            fullExceptionMessage = checkId(fullExceptionMessage);
            fullExceptionMessage = checkEmail(fullExceptionMessage);
            fullExceptionMessage = checkUserName(fullExceptionMessage);
            fullExceptionMessage = checkFullName(fullExceptionMessage);
            fullExceptionMessage = checkAge(fullExceptionMessage);
            fullExceptionMessage = checkNotes(fullExceptionMessage);
            fullExceptionMessage = checkAmountOfFollowers(fullExceptionMessage);

            if (!fullExceptionMessage.equals("")) throw new UserDataFailedException(fullExceptionMessage);

            service.addUser(user);
            return Response.ok(user);
        } catch (UserDataFailedException e) {
            return Response.fail(e.getMessage() + "\n" + request.getData());
        } catch (Exception e) {
            return Response.fail(exceptionList.get(0).getMessage());
        }
    }

    private String checkId(String message) {
        try {
            if (user.getId() == null) throw exceptionList.get(1);
            if (users.length > 0 && service.containId(user.getId())) throw exceptionList.get(1);
            return message;
        } catch (UserDataFailedException e) {
            return message + e.getMessage();
        }
    }

    private String checkEmail(String message) {
        try {
            String email = user.getEmail();
            if (email == null || email.isEmpty() || email.trim().isEmpty()) throw exceptionList.get(2);
            if (users.length > 0 && service.containEmail(email)) throw exceptionList.get(2);
            char[] invalidSymbols = {'<', '>', '(', ')', '[', ']', ',', ';', '\\', '/', '"', ' ', '*', '\''};
            for (char symbol : invalidSymbols) {
                if (email.trim().contains(String.valueOf(symbol))) throw exceptionList.get(2);
            }
            if (!email.contains("@")) throw exceptionList.get(2);
            return message;
        } catch (UserDataFailedException e) {
            return message + " " + e.getMessage();
        }
    }

    private String checkUserName(String message) {
        try {
            String userName = user.getUserName();
            if (userName == null || userName.isEmpty() || userName.trim().isEmpty()) throw exceptionList.get(3);
            if (users.length > 0 && service.containUserName(userName)) throw exceptionList.get(3);
            if (userName.trim().contains(" ")) throw exceptionList.get(3);
            char[] invalidSymbols = {'<', '>', '(', ')', '[', ']', ',', ';', '\\', '/', '"', '*', '\''};
            for (char symbol : invalidSymbols) {
                if (userName.contains(String.valueOf(symbol))) throw exceptionList.get(3);
            }
            if (!userName.toLowerCase().equals(userName)) throw exceptionList.get(3);
            return message;
        } catch (UserDataFailedException e) {
            return message + " " + e.getMessage();
        }
    }

    private String checkFullName(String message) {
        try {
            String fullName = user.getFullName();
            if (fullName == null || fullName.isEmpty() || fullName.trim().isEmpty()) throw exceptionList.get(4);
            String[] words = fullName.trim().split("\\s+");
            if (words.length != 2) throw exceptionList.get(4);
            if (Character.isLowerCase(words[0].charAt(0)) || Character.isLowerCase(words[1].charAt(0)))
                throw exceptionList.get(4);
            if (!words[0].substring(1).toLowerCase().equals(words[0].substring(1))) throw exceptionList.get(4);
            if (!words[1].substring(1).toLowerCase().equals(words[1].substring(1))) throw exceptionList.get(4);
            if (!isAllLetters(words[0]) && !isAllLetters(words[1])) throw exceptionList.get(4);
            return message;
        } catch (UserDataFailedException e) {
            return message + " " + e.getMessage();
        }
    }

    private boolean isAllLetters(String s) {
        for (int i = 0; i < s.length(); i++)
            if (!Character.isLetter(s.charAt(i))) return false;

        return true;
    }

    private String checkAge(String message) {
        try {
            if (user.getAge() == null || user.getAge() < 18 || user.getAge() >= 100) throw exceptionList.get(5);
            return message;
        } catch (UserDataFailedException e) {
            return message + " " + e.getMessage();
        }
    }

    private String checkNotes(String message) {
        try {
            if (user.getNotes() != null && !user.getNotes().isEmpty() && user.getNotes().length() >= 255)
                throw exceptionList.get(6);
            return message;
        } catch (UserDataFailedException e) {
            return message + " " + e.getMessage();
        }
    }

    private String checkAmountOfFollowers(String message) {
        try {
            if (user.getAmountOfFollowers() == null || user.getAmountOfFollowers() < 0) throw exceptionList.get(7);
            return message;
        } catch (UserDataFailedException e) {
            return message + " " + e.getMessage();
        }
    }

    private void setExceptionList() {
        exceptionList.add(new UserDataFailedException("Unsupported type of user"));
        exceptionList.add(new UserDataFailedException("Incorrect id"));
        exceptionList.add(new UserDataFailedException("Incorrect email"));
        exceptionList.add(new UserDataFailedException("Incorrect userName"));
        exceptionList.add(new UserDataFailedException("Incorrect fullName"));
        exceptionList.add(new UserDataFailedException("Incorrect age"));
        exceptionList.add(new UserDataFailedException("Incorrect notes"));
        exceptionList.add(new UserDataFailedException("Incorrect amountOfFollowers"));
    }
}
