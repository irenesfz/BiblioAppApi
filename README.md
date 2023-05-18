<div class="content">

# API Rest BIBLIOAPP - MySQL Server

<table class="content">

<tbody>

<tr>

<td class="rotulo">Database</td>

<td>biblioapp</td>

</tr>

<tr>

<td class="rotulo">Tables</td>

<td>eventos, 
libros,  
libro,  
socios,  
prestamos,  
reservas,  
interbibliotecario</td>

</tr>

<tr>

<td class="rotulo">Content-Type</td>

<td>application/json</td>

</tr>

<tr>

<td class="rotulo">Accept</td>

<td>application/json</td>

</tr>

</tbody>

</table>

</div>

<table>

<tbody>

<tr>

<th>MÉTODO</th>

<th>PATH</th>

<th>SEGURIDAD</th>

</tr>

<tr align="center">

<td colspan="3" class="titulo">LIBROS</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/libros/all](./v1/libros/all)</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/todo/{palabraABuscar}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/id/{id}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/isbn/{isbn}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/titulo/{titulo}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/autores/{autores}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/editorial/{editorial}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/anyopublicacion/{anyo}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/categoria/{categoria}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/disponibles</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libros/disponibles/{palabra}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/libros/disponibles](./v1/libros/disponibles)</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/libros/disponiblesReservar](./v1/libros/disponiblesReservar)</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libros</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/libros</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/libros/{isbn}</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">LIBRO</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/libro/all](./v1/libro/all)</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libro/{isbnLibro}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libro/ndisponibles/{isbnLibro}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libro/ud/isbn/{isbn}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libro/nreservas/{isbnLibro}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libro/reserva/{isbn}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/libro/prestamo/{isbn}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/libro/{isbn}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libro/</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libro/disponible/{id}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libro/disponibleReserva/{id}</td>

<td>🔒 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libro/noDisponibleReserva/{id}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libro/noDisponible/{id}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/libro/finaliza/{id}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/libro/</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/libro/unidad/isbn/{isbn}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/libro/isbn/{isbn}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/libro/{idLibro}</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">EVENTOS</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/eventos/all](./v1/eventos/all)</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/eventos/fecha/{fechaABuscar}</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/eventos</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/eventos</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/eventos/{id}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/eventos</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">INTERBIBLIOTECARIO</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/interbibliotecario/all](./v1/interbibliotecario/all)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/interbibliotecario</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/interbibliotecario</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/interbibliotecario/{id}</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">PRÉSTAMOS</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/prestamos/all](./v1/prestamos/all)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/prestamos/noFinalizados](./v1/prestamos/noFinalizados)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/prestamos/grid/all](./v1/prestamos/grid/all)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/prestamos/noFinalizados](./v1/prestamos/noFinalizados)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/prestamos/socio/{idSocio}</td>

<td>🔒 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/prestamos/fecha/{fecha}/{idSocio}</td>

<td>🔒 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/prestamos/noFinalizadoSocio/{idSocio}</td>

<td>🔒 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/prestamos/{idLibro}/{idSocio}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/prestamos/{idPrestamo}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/prestamos/{idPrestamo}</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">RESERVAS</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/reservas/all](./v1/reservas/all)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/reservas/grid/all](./v1/reservas/grid/all)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/reservas/socio/{idSocio}</td>

<td>🔓 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/reservas/noFinalizadaSocio/{idSocio}</td>

<td>🔓 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/reservas</td>

<td>🔓 Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/reservas/poneNotification/{idReserva}</td>

<td>🔓 Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/reservas/quitaNotification/{idReserva}</td>

<td>🔓 Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/reservas</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/reservas</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/reservas/add/{idSocio}/{idLibro}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/reservas/{idReserva}/{idLibro}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/reservas/{idReserva}</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">SOCIOS</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/socio/all](./v1/socio/all)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>[/biblioapp/v1/socio/grid](./v1/socio/grid)</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/socio/id/{idSocio}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/socio/dni/{dniSocio}</td>

<td>🔒 Trabajador y Usuario</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/socio</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/socio</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/socio/resetPassword/{idSocio}</td>

<td>🔒 Trabajador</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/socio/cambiaPassword/{dni}/{newPass}/{oldPass}</td>

<td>🔒 Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/socio/cambiaCorreo/{id}/{correo}</td>

<td>🔒 Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/socio/cambiaCategorias/{id}/{categorias}</td>

<td>🔒 Usuario</td>

</tr>

<tr>

<td class="cabecera">PUT</td>

<td>/biblioapp/v1/socio/cambiaImagenPerfil/{id}/{imagen}</td>

<td>🔒 Usuario</td>

</tr>

<tr>

<td class="cabecera">DELETE</td>

<td>/biblioapp/v1/socio/{idSocio}</td>

<td>🔒 Trabajador</td>

</tr>

<tr align="center">

<td colspan="3" class="titulo">AUTH</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/jwt</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/jwt/check</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">GET</td>

<td>/biblioapp/v1/jwt/create-session</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/jwt/auth</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/jwt/logout</td>

<td>🔓</td>

</tr>

<tr>

<td class="cabecera">POST</td>

<td>/biblioapp/v1/jwt/destroysession</td>

<td>🔓</td>

</tr>

</tbody>

</table>
