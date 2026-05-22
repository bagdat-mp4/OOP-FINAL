package comparators;

import models.ResearchPaper;
import java.util.Comparator;

/**
 * Comparator for sorting research papers by citations (descending)
 */
public class CitationsComparator implements Comparator<ResearchPaper> {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getCitations(), a.getCitations()); // descending
    }

}