package org.sid.exception;

public class ActiviteException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public ActiviteException(String e) {
        super(e);
    }

    public ActiviteException(long id) {
        super("L' activite donc l'id egale " + id +" n'existe pas");
    }
}
