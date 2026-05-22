package models;

import java.io.*;
import java.util.*;


public interface Researcher {

    double calculateHIndex();

    void printPapers(Comparator<ResearchPaper> comparator);

    List<ResearchPaper> getPapers();

}