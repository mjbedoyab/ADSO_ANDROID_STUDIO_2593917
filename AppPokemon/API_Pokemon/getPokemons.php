<?php

// URL de la API de Pokémon para obtener la lista de Pokémon
$url = "https://pokeapi.co/api/v2/pokemon/";

// Realizar la solicitud GET
$data = file_get_contents($url);

// Decodificar los datos JSON
$pokemon_data = json_decode($data);

// Array para almacenar los detalles de los Pokémon
$pokemons = [];

// Iterar sobre cada Pokémon en la lista
foreach ($pokemon_data->results as $pokemon) {
    // Realizar una solicitud GET para obtener más detalles sobre el Pokémon
    $pokemon_details_data = json_decode(file_get_contents($pokemon->url));

    // Obtener el nombre, peso, altura y habilidades del Pokémon
    $pokemon_name = $pokemon_details_data->name;
    $pokemon_weight = $pokemon_details_data->weight;
    $pokemon_height = $pokemon_details_data->height;
    
    // Obtener las habilidades del Pokémon
    $pokemon_abilities = [];
    foreach ($pokemon_details_data->abilities as $ability) {
        $pokemon_abilities[] = $ability->ability->name;
    }

    // Obtener las imágenes específicas del Pokémon (normal y brillante)
    $pokemon_images = [
        "back_default" => $pokemon_details_data->sprites->back_default,
        "back_shiny" => $pokemon_details_data->sprites->back_shiny,
        "front_shiny" => $pokemon_details_data->sprites->front_shiny,
        "front_default" => $pokemon_details_data->sprites->front_default
    ];

    // Agregar los detalles del Pokémon al array
    $pokemons[] = [
        "name" => $pokemon_name,
        "images" => $pokemon_images,
        "weight" => $pokemon_weight,
        "height" => $pokemon_height,
        "abilities" => $pokemon_abilities
    ];
}

// Construir respuesta JSON
$response = [
    "status" => true,
    "message" => "Pokémon found",
    "pokemons" => $pokemons
];

// Imprimir respuesta JSON
header('Content-Type: application/json');
echo json_encode($response);
?>
