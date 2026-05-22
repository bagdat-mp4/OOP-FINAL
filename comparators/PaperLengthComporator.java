package comparators;

import models.ResearchPaper;
import java.util.Comparator;

/**
 * Comparator for sorting research papers by length/pages (longest first)
 */
public class PaperLengthComporator implements Comparator<ResearchPaper> {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getPages(), a.getPages()); // longest first
    }

}