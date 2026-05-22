package comparators;

import models.ResearchPaper;
import java.util.Comparator;


public class DateComparator implements Comparator<ResearchPaper> {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return b.getDatePublished().compareTo(a.getDatePublished());
    }
}
