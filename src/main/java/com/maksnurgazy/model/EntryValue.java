package com.maksnurgazy.model;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record EntryValue<K extends Serializable, V extends Serializable>(K k, V v) implements Serializable {
	@Serial
	private static final long serialVersionUID = -9129961926218084845L;
}
