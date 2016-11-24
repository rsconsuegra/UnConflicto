package org.underpressureapps.unconflicto;

import java.io.Serializable;
import java.util.List;

public class Schedule implements Serializable{
    List<Block> Blocks;

    public Schedule(List<Block> Blocks) {
        this.Blocks = Blocks;
    }

    public Block getBlock(int i) {
        return Blocks.get(i);
    }

    public List<Block> getBlocks() {
        return Blocks;
    }
}

class Block implements Serializable{

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

    public void sCourseName(String courseName) {
        CourseName = courseName;
    }

    public void sDay(String day) {
        Day = day;
    }

    public void sStartHour(String startHour) {
        StartHour = startHour;
    }

    public void sEndHour(String endHour) {
        EndHour = endHour;
    }

    public String getDay() {
        return Day;
    }



    public String getEndHour() {
        return EndHour;
    }



    public String getStartHour() {
        return StartHour;
    }


}
