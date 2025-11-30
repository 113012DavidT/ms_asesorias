package com.uteq.coordinadores.snapshot.client;

import com.uteq.coordinadores.snapshot.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-admin", contextId = "adminSnapshotClient")
public interface AdminSnapshotClient {

    @GetMapping("/api/admin/usuarios/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable Long id);
}
