package controllers;

import models.User;
import models.Course;
import models.ResearchPaper;
import models.ResearcherDecorator;
import core.DataStore;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchController {

    private final DataStore ds = DataStore.getInstance();

    public List<User> searchUsersByRegex(String pattern) {
        List<User> results = new ArrayList<>();
        try {
            Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            for (User u : ds.getUsers()) {
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
            for (Course c : ds.getCourses()) {
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
            for (ResearcherDecorator rd : ds.getResearcherMap().values()) {
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
