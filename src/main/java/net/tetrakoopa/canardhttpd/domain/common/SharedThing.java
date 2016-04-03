package net.tetrakoopa.canardhttpd.domain.common;

import java.util.Date;


public interface SharedThing {
	
	enum ShareStatus {
		NOT_SHARED, SHARED, SHARED_BUT_REMOVAL_REQUESTED
	};
	

	class Tag  {

		public enum SystemAttribute {
			PUBLIC
		};

		private final SystemAttribute attribute;
		private final String name;
		public Tag(SystemAttribute attribute) {
			if (attribute==null)
				throw new NullPointerException("Tag attribute cannot be null");
			this.name = attribute.name();
			this.attribute = attribute;
		}
		public Tag(String name) {
			if (name==null)
				throw new NullPointerException("Tag name cannot be null");
			this.name = name;
			this.attribute = null;
		}
		public String getName() {
			return name;
		}
		@Override
		public boolean equals(Object object) {
			if (object==null)
				return false;
			if (!(object instanceof Tag))
				return false;
			final Tag tag = (Tag)object;
			if (tag.attribute != this.attribute)
				return false;
			if ( tag.name==null && this.name == null)
				return true;
			if ( tag.name!=null && this.name != null)
				return tag.name.equals(this.name);
			return false;
		}

		@Override
		public int hashCode() {
			return (name == null ? 0 : name.hashCode() ) ^ (attribute == null ? 0 : attribute.hashCode() );
		}
		
		public boolean isSystem(SystemAttribute attribute) {
			return this.attribute == attribute;
		}
		public boolean isSystem() {
			return this.attribute != null;
		}
		
		public SystemAttribute getAttribute() {
			return attribute;
		}
	}

	String getName();

	String getType();

	String getComment();
	
	Date getShareDate();

	void setShareDate(Date date);

}
