Eos
====

Note
------

Eos is the result of a research project. To support our research, please cite one of our papers instead of referencing the e.g. repo in scientific articles. You can find an overview of papers about Eos here. If you are not sure which paper to cite, we recommend this one:

PAPER

Thanks!

## Setup

### Prerequisites

* JDK (>= 17)
* Apache Maven (>= 3.8.0)
* docker and docker-compose if you want to use docker
* Postgres if not using docker
* optionally: openEHR platform, otherwise there is the option to setup one down below

### Setting up an OMOP Db and connect it to an openEHR platform

1. clone repo `git clone --recursive`
2. Use Common Data model sqls from `setup/ddls/` or download
   them [here](https://github.com/OHDSI/CommonDataModel/tree/master/inst/ddl/5.4/postgresql). If downloaded: Delete all
   @cdmDatabaseSchema in every file and the version number in the file name (e.g. OMOPCDM_postgresql_5.4_ddl.sql ->  OMOPCDM_postgresql_ddl.sql)
3. Download Vocabs from https://athena.ohdsi.org/, you need to create an Account for that, afterwards copy them
   to `setup/vocab/`
   <br> Recommended are:

| ID  | CDM	Code (cdm v5)       | Name                                                                                                   |
|-----|-------------------------|--------------------------------------------------------------------------------------------------------|
| 12  | Gender                  | OMOP Gender                                                                                            |
| 13  | Race                    | Race and Ethnicity Code Set (USBC)                                                                     |
| 71  | ABMS                    | Provider Specialty (American Board of Medical Specialties)                                             |
| 71  | <br/>Medicare Specialty | Medicare Specialty	Medicare provider/supplier specialty codes (CMS)<br/>                               |
| 6   | LOINC                   | Logical Observation Identifiers Names and Codes (Regenstrief Institute)                                |
| 21  | ATC	WHO                 | Anatomic Therapeutic Chemical Classification                                                           |                                                   
| 2   | ICD9CM                  | International Classification of Diseases, Ninth Revision, Clinical Modification, Volume 1 and 2 (NCHS) |
| 82  | RxNorm Extension        | RxNorm Extension (OHDSI)                                                                               |                                          
| 14  | CMS Place of Service    | Place of Service Codes for Professional Claims (CMS)                                                   |                            
| 1   | SNOMED                  | Systematic Nomenclature of Medicine - Clinical Terms (IHTSDO)                                          |                             
| 4   | CPT4                    | Current Procedural Terminology version 4 (AMA)                                                         |                                       
| 43  | Revenue Code            | UB04/CMS1450 Revenue Codes (CMS)                                                                       |                          
| 44  | Ethnicity               | OMOP Ethnicity                                                                                         |                           
| 65  | Currency                | International Currency Symbol (ISO 4217)                                                               |                       
| 9   | NDC                     | National Drug Code (FDA and manufacturers)                                                             |                          
| 70  | ICD10CM                 | International Classification of Diseases, Tenth Revision, Clinical Modification (NCHS)                 |       
| 47  | NUCC                    | National Uniform Claim Committee Health Care Provider Taxonomy Code Set (NUCC)                         |                
| 5   | HCPCS                   | Healthcare Common Procedure Coding System (CMS)                                                        |           
| 8   | RxNorm                  | RxNorm (NLM)                                                                                           |          
| 50  | SPL                     | Structured Product Labeling (FDA)                                                                      |         
| 128 | OMOP Extension          | OMOP Extension (OHDSI)                                                                                 |    
| 3   | ICD9Proc                | International Classification of Diseases, Ninth Revision, Clinical Modification, Volume 3 (NCHS)       |

### With Docker
4. Check if the OMOP_CDM_vocabulary_load sql script contains all your csvs.
5. run one of the compose files.
   You may choose either between running only the OMOP DB or also an ehrbase (opensource openEHR platform). <br>
   **Setup may take a while since all the vocabularies are loaded into the database**
#### only DB
5. run in `setup/`
```shell script
  docker-compose -f docker-compose-only-cdm.yml up
```
Port can be changed in the docker-compose current port is 5433.

#### including ehrbase
5. run in `setup/`
```shell script
  docker-compose -f docker-compose-cdm+ehrbase.yml up
```

### Without Docker

4. Start your postgres database (in this walktrough the example port is **5433**, so it is not in conflict with other
   postgres dbs from e.g. the openEHR platform)

5. execute the command in `setup/ddls`

```shell script
   $ psql -h localhost --port=5433 -U postgres -W --dbname=YOUR_DB_NAME --file=db_setup.sql
   ```


you can empty the folder `setup/vocab` afterwards if you wish , since the files require a around 4-5 Gbs of space

### Running OMOP bridge

1. Make sure a openEHR platform and the database is running

2. Build the application

```shell script
$ mvn clean install
```

2. configure database in `src/main/resources/application.yml`

```yml
   datasource:
     password: YOUR_PASSWORD
     username: YOUR_USERNAME
     url: jdbc:postgresql://localhost:5433/YOUR_SCHEMA #default is public
```

3. configure openEHR platform in `src/main/resources/application.yml`. <br>
   If you do not have setup an openEHR platform you can use [ehrbase](https://github.com/ehrbase/ehrbase).
   (See [here](https://ehrbase.readthedocs.io/en/latest/03_development/04_docker_images/01_ehrbase/02_use_image/index.html#run-ehrbase-db-with-docker-compose)
   for a guide how to set it up with docker-compose.)

```yml
  ehrbase:
    base-url: YOUR_PLATFORM_URL
    security:
      type: basic # you can also use other sec depending on what you want
      user:
        name: YOUR_USERNAME
        password: YOUR_PASSWORD
```
4. Running scripts for Condition_Era, Drug_Era and Observation_Period:
   provided in folder, otherwise use sqlrenderer to generate new.

## Mappings
We currently support CDM v5.4, if more tooling is provided v6 will be added in the future.

### implemented Mappings
See [here](https://github.com/SevKohler/OMOCL)

### Usable with declarative mappings

| CDM table            | Supported               |
|----------------------|-------------------------|
| OBSERVATION_PERIOD   | automatically generated |
| VISIT_OCCURRENCE     | automatically generated |
| VISIT_DETAIL         |                         |
| CONDITION_OCCURRENCE | x                       | 
| DRUG_EXPOSURE        | x                       |
| PROCEDURE_OCCURRENCE | x                       |
| DEVICE_EXPOSURE      | x                       |
| MEASUREMENT          | x                       |
| OBSERVATION          | x                       |
| DEATH                | x                       | 
| NOTE                 |                         | 
| NOTE_NLP             |                         | 
| SPECIMEN             | x                       |
| FACT_RELATIONSHIP    |                         |
| DRUG_ERA             | automatically generated |
| CONDITION_ERA        | automatically generated |



DISCLAIMER:

- INTERVAL_EVENT is currently only mapping the event time
- Interval of Quantity is not supported
- Multiplication is currently only supported for DrugExposure quantity, will be added to other configs on demand.
- Dv_Proportion only type 2 is supported for unit due to lack of sample data. If samples are provided this can be added.
- CustomConverters are not supported for Person Conversions
- range low range high and operator will map normal range and magnitude status if DV Quantity is provided and NOT magnitude.

Special thanks
---
We thank the Georgia Tech Research Institute for providing jpa classes for OMOP and vita systems for their free accessible openEHR tooling.
