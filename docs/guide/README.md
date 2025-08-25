# Taxes Exclusion

## Introducción

Mediante ésta aplicación se intenta resolver el problema de identificar usuarios que deberían ser excluidos de
retenciones de un determinado impuesto. <br>
Para lo cual se disponibilizan servicios que, integrados con aplicación de admin, permiten:

- Generar altas de exclusiones.
- Generar altas por usuarios en determinadas exclusiones.
- Generar alta/baja masiva de usuarios en exclusiones.
- Realizar consultas:
    - De usuarios excluidos.
    - Del histórico por usuario.
    - Exclusiones actuales por site.

Independientemente del site en donde opere el usuario.
Logrando así evitar el trabajo manual de desarrolladores, tener un acceso controlado y una auditoria de los cambios que
se realicen.

# Stakeholders

# Admin de exclusiones

Mediante el administrador de exclusiones se puede:

- Exceptuar un seller por identificador fiscal de una o más retenciones.
- Poder establecer un período de vigencia para la exclusion, especificando fecha de inicio
  y fecha de fin de la exclusión.
- Tener control de acceso de usuarios y auditoría de cambios respecto al alta, baja y
  modificación de exclusiones.

### Reglas

1. Un Seller solamente puede tenr un regisrto de exclsuión activo por vez, una nueva carga sobreescriba a la anterior.
2. Una exclusión afecta a todos los userId relacionados al identificador fiscal del Seller.
3. Las solicitudes de exclusiones tienen que ser aprobadas para que sean efectivamente aplicadas.
4. Una exclusión aprobada pasa a estar vigente el día siguiente al de su aprobación.
5. Un creador de solicitudes no puede ser un aprobador.
6. No puede haber más de una solicitud pendiente por identificador fiscal.

### Estados de una solicitud

| Estad     | Descripción                                                                             |
|-----------|-----------------------------------------------------------------------------------------|
| Pendiente | La solicitud todavía no fue aprobada ni rechazada                                       |
| Aprobada  | La solicitud fue procesada, por lo que no puede editarse                                |
| Rechazada | La solicitud fue procesada, por lo que no puede editarse                                |
| Expirada  | La solicitud expiro por superarse el tiempo de tolerancia de 30 días para ser procesada |

---

## Fury

---

## Autores
