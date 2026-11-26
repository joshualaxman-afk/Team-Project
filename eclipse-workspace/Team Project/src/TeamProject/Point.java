package TeamProject;

import java.util.Objects;

public record Point(int x, int y) {
	  
	  @Override
	  public boolean equals(Object otherObject) {
		if(this == otherObject) {
			return true;
		}
		
	    if(otherObject.getClass() != Point.class) {
	      return false;
	    }

	    Point other = (Point)(otherObject);
	    return other.x() == this.x() && other.y() == this.y();
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(this.x, this.y);
	  }
	}