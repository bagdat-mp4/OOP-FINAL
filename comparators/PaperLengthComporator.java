package comparators;

import models.ResearchPaper;
import java.util.Comparator;


public class PaperLengthComporator implements Comparator<ResearchPaper> {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getPages(), a.getPages()); 
    }

}