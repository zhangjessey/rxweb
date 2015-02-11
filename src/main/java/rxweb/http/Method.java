package rxweb.http;

import rxweb.support.Assert;

public class Method {

	public static final Method GET = new Method("GET");
	public static final Method POST = new Method("POST");
	public static final Method PUT = new Method("PUT");
	public static final Method PATCH = new Method("PATCH");
	public static final Method DELETE = new Method("DELETE");
	public static final Method OPTIONS = new Method("OPTIONS");
	public static final Method HEAD = new Method("HEAD");
	public static final Method TRACE = new Method("TRACE");
	public static final Method CONNECT = new Method("CONNECT");
	public static final Method BEFORE = new Method("BEFORE");
	public static final Method AFTER = new Method("AFTER");

	private final String name;

	public Method(String name) {
		Assert.hasText(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Method method = (Method) o;

		if (!name.equals(method.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}