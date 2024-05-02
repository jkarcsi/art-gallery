package jkarcsi.utils.constants;

public interface AccessLevel {
    String COMMON = "hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')";
    String CLIENT = "hasRole('ROLE_CLIENT')";
    String ADMIN = "hasRole('ROLE_ADMIN')";
}
