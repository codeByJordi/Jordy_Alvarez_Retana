package com.ufide.cursosapp.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;

    public record LoginRequest(String nombre, String password){}
    public record LoginResponse(String token) {}

    @PostMapping("/login") //el post login no existe, pero aqui lo creamos para el jwt
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        )

        String rol = auth,getAuthorities().stream().findFirts().map(GrantedAuthority::getAuthority).orElse("");

        String token = jwtService.generarToken(request.username(), rol);
        return ResponseEntity,ok(new LoginResponse(token));
    }
}
