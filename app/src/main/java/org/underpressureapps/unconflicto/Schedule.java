package org.underpressureapps.unconflicto;

import java.io.Serializable;
import java.util.List;

public class Schedule implements Serializable{
    List<Block> Blocks;

    public Schedule(List<Block> blocks) {
        Blocks = blocks;
    }

    public Block getBlock(int i) {
        return Blocks.get(i);
    }

    public List<Block> getBlocks() {
        return Blocks;
    }
}

class Block
{
    public String CourseName;
    public String Day;
    public String StartHour;
    public String EndHour;

    public Block() {

    }

    public Block(String courseName, String day, String startHour, String endHour) {
        CourseName = courseName;
        Day = day;
        StartHour = startHour;
        EndHour = endHour;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getEndHour() {
        return EndHour;
    }

    public void setEndHour(String endHour) {
        EndHour = endHour;
    }

    public String getStartHour() {
        return StartHour;
    }

    public void setStartHour(String startHour) {
        StartHour = startHour;
    }
}
