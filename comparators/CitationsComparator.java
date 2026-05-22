package comparators;

import models.ResearchPaper;
import java.util.Comparator;


public class CitationsComparator implements Comparator<ResearchPaper> {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getCitations(), a.getCitations());
    }
}
