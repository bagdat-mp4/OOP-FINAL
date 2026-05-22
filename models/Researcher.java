package models;

import java.io.*;
import java.util.*;

/**
 * Researcher interface for research capabilities
 */
public interface Researcher {

    double calculateHIndex();

    void printPapers(Comparator<ResearchPaper> comparator);

    List<ResearchPaper> getPapers();

}