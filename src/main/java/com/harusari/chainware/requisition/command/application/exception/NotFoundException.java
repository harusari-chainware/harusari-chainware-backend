// 📁 com.harusari.chainware.requisition.command.application.exception.NotFoundException
package com.harusari.chainware.requisition.command.application.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
