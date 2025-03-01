package math;

public class MutableBoolean {
	public boolean val;

	public MutableBoolean(boolean val) {
		this.val = val;
	}

	@Override
	public int hashCode() {
		return val ? 1 : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutableBoolean other = (MutableBoolean) obj;
		return val == other.val;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(val);
	}

}
