package thetadev.constructionwand.basics.options;

import com.google.common.base.Enums;

public enum EnumRandom implements IEnumOption
{
	YES,
	NO;

	private static EnumRandom[] vals = values();

	public IEnumOption fromName(String name) {
		return Enums.getIfPresent(EnumRandom.class, name.toUpperCase()).or(this);
	}

	public EnumRandom next(boolean dir) {
		int i = this.ordinal() + (dir ? 1:-1);
		if(i < 0) i += vals.length;

		return vals[i % vals.length];
	}

	public int getOrdinal() {
		return ordinal();
	}

	public String getOptionKey() {
		return "random";
	}

	public String getValue() {
		return this.name().toLowerCase();
	}
}