package models;

import java.io.*;
import java.util.*;

/**
 * 
 */
public interface Subscriber {

    /**
     * @param journalName 
     * @param paper 
     * @return
     */
    public void update(String journalName, models.ResearchPaper paper);

}