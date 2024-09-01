#!/bin/bash

# Verificar se o Java está instalado
if ! command -v java &> /dev/null
then
    echo "Java não encontrado. Por favor, instale o Java 11 ou superior."
    exit 1
fi

# Verificar a versão do Java
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
REQUIRED_VERSION="11"
if [[ "$JAVA_VERSION" < "$REQUIRED_VERSION" ]]
then
    echo "Versão do Java instalada: $JAVA_VERSION. Por favor, instale o Java 11 ou superior."
    exit 1
fi

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null
then
    echo "Maven não encontrado. Por favor, instale o Maven."
    exit 1
fi

# Instalar dependências do Maven
echo "Instalando dependências do Maven..."
mvn clean install

# Iniciar a aplicação Spring Boot
echo "Iniciando a aplicação Spring Boot..."
mvn spring-boot:run
