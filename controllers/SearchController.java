package controllers;

import models.*;
import core.DataStore;
import java.util.*;
import java.util.regex.*;

public class SearchController {

    public List<User> searchUsersByRegex(String pattern) {
        List<User> results = new ArrayList<>();
        try {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            for (User u : DataStore.getInstance().getUsers()) {
                String searchStr = u.getFirstName() + " " + u.getLastName() + " " + u.getEmail();
                if (p.matcher(searchStr).find()) results.add(u);
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Invalid regex pattern: " + e.getMessage());
        }
        return results;
    }

    public List<Course> searchCoursesByRegex(String pattern) {
        List<Course> results = new ArrayList<>();
        try {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            for (Course c : DataStore.getInstance().getCourses()) {
                String searchStr = c.getName() + " " + c.getCourseCode();
                if (p.matcher(searchStr).find()) results.add(c);
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Invalid regex: " + e.getMessage());
        }
        return results;
    }

    public List<ResearchPaper> searchPapersByRegex(String pattern) {
        List<ResearchPaper> results = new ArrayList<>();
        try {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            for (ResearcherDecorator rd : DataStore.getInstance().getResearcherMap().values()) {
                for (ResearchPaper paper : rd.getPapers()) {
                    String searchStr = paper.getTitle() + " " + paper.getJournal();
                    if (p.matcher(searchStr).find()) results.add(paper);
                }
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Invalid regex: " + e.getMessage());
        }
        return results;
    }
}
