package comparators;

import models.ResearchPaper;
import java.util.Comparator;

/**
 * Comparator for sorting research papers by date (newest first)
 */
public class DateComparator implements Comparator<ResearchPaper> {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return b.getDatePublished().compareTo(a.getDatePublished()); // newest first
    }

}