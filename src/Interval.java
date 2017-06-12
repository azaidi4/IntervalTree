///////////////////////////////////////////////////////////////////////////////
//	Semester:		CS367 Spring 2017
//  PROJECT:		P4
//  FILE:			Interval.java
//
//  TEAM:    16;
//
// Authors:
// Author1: Ahmad Zaidi, azaidi4@wisc.edu, azaidi4, LEC001
// Author2: Devin Samaranayake, dsamaranayak@wisc.edu>, dsamaranayak, LEC001
//
///////////////////////////////////////////////////////////////////////////////
public class Interval<T extends Comparable<T>> implements IntervalADT<T> {

    private T start;
    private T end;
    private String label;

    public Interval(T start, T end, String label) {
        this.start = start;
        this.end = end;
        this.label = label;
    }

    @Override
    public T getStart() { return start; }

    @Override
    public T getEnd() { return end; }

    @Override
    public String getLabel() { return label; }

    @Override
    public boolean overlaps(IntervalADT<T> other) {
        if(other == null) throw new IllegalArgumentException();

        // if b < c OR d < a, not overlapping
        return !( end.compareTo(other.getStart()) == -1  ||
                other.getEnd().compareTo(start) == -1 );
    }

    @Override
    public boolean contains(T point) {
        // return a <= point <= b

       /* interval is open
        return getStart().compareTo(point) == -1 &&
                getEnd().compareTo(point) == 1;
                */
        return getStart().compareTo(point) <= 0 &&
                getEnd().compareTo(point) >= 0;
    }

    @Override
    public int compareTo(IntervalADT<T> other) {

        if(start.compareTo(other.getStart()) == 0){
            return end.compareTo(other.getEnd());
        }
        return start.compareTo(other.getStart());
    }

    @Override
    public String toString() {
        return label + " [" + start + ", " + end + "]";
    }
}
