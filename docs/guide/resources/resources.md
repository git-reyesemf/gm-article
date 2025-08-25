# Recursos

## Endpoints

El proyecto expone un serie de endpoints para la creación y la consulta de exclusiones,
por usuario, por impuesto y por site.

### Consulta de site's disponibles

#### cURL:

```cURL
curl -X GET \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/site \
```

| Ruta  | Obligatoriedad | Valor                                    | 
|-------|----------------|------------------------------------------|
| scope | mandatorio     | Scope a utilizar. Puede ser test o prod. |

---

### Consulta de impuestos a excluir

#### cURL:

```cURL
curl -X GET \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/tax \
```

| Ruta  | Obligatoriedad | Valor                                    | 
|-------|----------------|------------------------------------------|
| scope | mandatorio     | Scope a utilizar. Puede ser test o prod. |

---

### Consulta de usuario excluido

#### cURL:

```cURL
curl -X GET \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/{site}/{identifierType}/{identifierCode} \
```

| Parámetro      | Obligatoriedad | Valor                                             | 
|----------------|----------------|---------------------------------------------------|
| site           | Mandatorio     | Site                                              |
| identifierType | Mandatorio     | Tipo de documento del contribuyente               |
| intifierCode   | Mandatorio     | Número de documento del contribuyente sin guiones |
| scope          | Mandatorio     | Scope a utilizar. Puede ser test o prod.          |

---

### Consulta de histórico de usuario excluido

#### cURL:

```cURL
curl -X GET \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/{site}/{identifierType}/{identifierCode}?history=true&startDate={startDate}&endDate={endDate}&page={page} \
```

| Parámetro      | Obligatoriedad | Valor                                             | 
|----------------|----------------|---------------------------------------------------|
| site           | Mandatorio     | Site                                              |
| identifierType | Mandatorio     | Tipo de documento del contribuyente               |
| intifierCode   | Mandatorio     | Número de documento del contribuyente sin guiones |
| intifierCode   | Mandatorio     | Número de documento del contribuyente sin guiones |
| history        | Opcional       | Si se desea habilitar el histórico, true o false  |
| startDate      | Opcional       | Fecha *desde* de la búsqueda, default MIN         |
| endDate        | Opcional       | Fecha *hasta* de la búsqueda, default MAX         |
| page           | Opcional       | Número de página                                  |
| scope          | Mandatorio     | Scope a utilizar. Puede ser test o prod.          |

---

### Validación de una exclusión

#### cURL:

```cURL
curl -X POST \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/authorized/{site}/{identificationType}/{identificationCode}?apply=false \
-H 'X-Client-Id: key'  \
```

| Parámetro      | Obligatoriedad | Valor                                                                               | 
|----------------|----------------|-------------------------------------------------------------------------------------|
| site           | Mandatorio     | Site                                                                                |
| identifierType | Mandatorio     | Tipo de documento del contribuyente                                                 |
| intifierCode   | Mandatorio     | Número de documento del contribuyente sin guiones                                   |
| apply          | Opcional       | Si aplica o no los cambios, *false* para que haga validación previa correspondiente |
| scope          | Mandatorio     | Scope a utilizar. Puede ser test o prod.                                            |
| X-Client-Id    | Mandatorio     | Client id key                                                                       |

#### Body:

- start_date: fecha desde exclusión
- end_date: fecha hasta exclusión
- taxes: lista de impuesto a excluir el usuario

Ejemplo:

```json
{
     "motive": "Alta por Tierra del Fuego",
     "issue": "1",
     "taxes": [{
       "site_id": "MLA",
       "identifier_type": "withholding",
       "identifier_code": "34",
       "start_date": "2020-12-01T10:12:44",
       "end_date": "2020-12-14T10:12:44"
     }]
}
```

---

### Alta de una exclusión

#### cURL:

```cURL
curl -X POST \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/authorized/{site}/{identifierType}/{identifierCode}?apply=true \
-H 'X-Client-Id: key'  \
```

| Parámetro      | Obligatoriedad | Valor                                                                    | 
|----------------|----------------|--------------------------------------------------------------------------|
| site           | Mandatorio     | Site                                                                     |
| identifierType | Mandatorio     | Tipo de documento del contribuyente                                      |
| intifierCode   | Mandatorio     | Número de documento del contribuyente sin guiones                        |
| apply          | Opcional       | Si aplica o no los cambios, *true* para que haga el alta correspondiente |
| scope          | Mandatorio     | Scope a utilizar. Puede ser test o prod.                                 |
| X-Client-Id    | Mandatorio     | Client id key                                                            |

#### Body:

- start_date: fecha desde exclusión
- end_date: fecha hasta exclusión
- issue: id del tramite o ticket
- taxes: lista de impuesto a excluir el usuario
- motive: motivo de la alta

```json
    {
     "issue": "1",
     "motive": "Alta por Tierra del Fuego",
     "taxes": [{
       "site_id": "MLA",
       "identifier_type": "WITHHOLDING",
       "identifier_code": "34",
       "start_date": "2020-12-01T10:12:44",
       "end_date": "2020-12-14T10:12:44"
     }]
    }
```

---

### Consulta todas las exclusiones actuales de un Site

#### cURL:

```cURL
curl -X GET \
https://{{scope}}-taxes-core.melioffice.com/taxes/exclusion/{site}?page=0  
```

| Parámetro | Obligatoriedad | Valor                           | 
|-----------|----------------|---------------------------------|
| site      | Mandatorio     | Site                            |
| page      | Opcional       | Si no se agrega el default es 0 |

---

### Mensaje al tópico

- Topic name test: exclusion-event-topic-test
- Topic name prod: exclusion-event-topic-prod

#### Esquema del mensaje:

| Propiedad                            | Tipo     | Nota                                                                                         |
|--------------------------------------|----------|----------------------------------------------------------------------------------------------|
| tax_payer                            | Objeto   | Nodo del contribuyente                                                                       |
| tax_payer.identifier_type            | Sring    | Tipo de documento identificador                                                              |
| tax_payer.identifier_code            | String   | Número de identificación del contribuyente                                                   |
| tax_payer.sie_id                     | String   | Sitio                                                                                        |
| tax_payer.exclusion                  | Objeto   | Nodo de exclusion                                                                            |
| tax_payer.exclusion.event            | String   | Evento descriptivo que esta sucediendo con la exclusion (UPDATE o CREATE)                    |
| tax_payer.exclusion.taxes.end_date   | DateTime | Fecha de inicio de la exclusión del contribuyente, <br> *formato "yyyy-MM-dd'T'HH.mm:ss'Z'"* |
| tax_payer.exclusion.taxes.start_date | DateTime | Fecha de fin de la exclusión del contribuyente, <br> *formato "yyyy-MM-dd'T'HH.mm:ss'Z'"*    |
| tax_payer.exclusion.current          | Boolean  | Indica si la exclusión del contribuyente es la vigente                                       |
| tax_payer.exclusion.taxes            | Nodo     | Lista de impuestos de las que se excluye al contribuyente                                    |
| identifier_type                      | String   | Identificación del tipo de impuesto que se excluye                                           |
| identifier_code                      | String   | tax_id del impuesto                                                                          |

#### Ej.

```json
{
  "tax_payer": {
    "identifier_type": "CUIT",
    "site_id": "MLA",
    "identifier_code": "20394823236",
    "exclusion": {
      "current": true,
      "event": "CREATE",
      "taxes": [
        {
          "identifier_type": "WITHHOLDING",
          "identifier_code": "1",
          "end_date": "2021-08-05T13:56:46.603399",
          "start_date": "2021-07-06T13:56:46.603399"
        },
        {
          "identifier_type": "WITHHOLDING",
          "identifier_code": "2",
          "end_date": "2021-08-05T13:56:46.603399",
          "start_date": "2021-07-06T13:56:46.603399"
        }
      ]
    }
  }
}
```

#### Lista de tag's

| Tag        |
|------------|
| siteid:mla |
| siteid:mlu |
| siteid:mco |

---

## Métricas y Monitores

- [Dashboard](https://app.datadoghq.com/dashboard/zeu-qp9-ace/ta)
- [Monitores](https://app.datadoghq.com/monitors/manage?q=tag%3A%22project%3Ataxes-exclusion%22&id=81182316)
