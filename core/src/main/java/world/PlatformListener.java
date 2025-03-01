package world;

import entities.Entity;

public interface PlatformListener {
	public void endContact(Entity entity);

	public static PlatformListener empty() {
		return new PlatformListener() {
			@Override
			public void endContact(Entity entity) {
			}
		};
	}
}
