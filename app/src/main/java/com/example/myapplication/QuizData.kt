package com.aadil.quiz

enum class QuestionKind { PICTURE, LETTER, TEXT }

data class QuizChoice(
    val text: String,
    val visual: String = text,
)

data class QuizQuestion(
    val prompt: String,
    val instruction: String,
    val choices: List<QuizChoice>,
    val correctIndex: Int,
    val kind: QuestionKind = QuestionKind.PICTURE,
    val hero: String = "",
    val tip: String = "",
)

data class QuizSection(
    val id: String,
    val title: String,
    val icon: String,
    val questions: List<QuizQuestion>,
)

private fun pictureQuestion(
    prompt: String,
    answer: String,
    vararg choices: Pair<String, String>,
    tip: String = "",
) = QuizQuestion(
    prompt = prompt,
    instruction = "Look carefully and tap the correct picture.",
    choices = choices.map { QuizChoice(it.first, it.second) },
    correctIndex = choices.indexOfFirst { it.first == answer },
    tip = tip,
)

private fun textQuestion(
    prompt: String,
    answer: String,
    vararg choices: String,
    hero: String = "",
    tip: String = "",
) = QuizQuestion(
    prompt = prompt,
    instruction = "Listen, then choose the correct answer.",
    choices = choices.map { QuizChoice(it) },
    correctIndex = choices.indexOf(answer),
    kind = QuestionKind.TEXT,
    hero = hero,
    tip = tip,
)

private fun letterQuestion(
    word: String,
    hero: String,
    answer: String,
    vararg letters: String,
) = QuizQuestion(
    prompt = word,
    instruction = "Which letter completes the word? Tap the missing letter.",
    choices = letters.map { QuizChoice(it) },
    correctIndex = letters.indexOf(answer),
    kind = QuestionKind.LETTER,
    hero = hero,
    tip = "The missing letter is $answer.",
)

val quizSections = listOf(
    QuizSection(
        id = "alphabet",
        title = "Alphabet Fun",
        icon = "🔤",
        questions = listOf(
            letterQuestion("C _ T", "🐱", "A", "A", "O", "E", "I"),
            letterQuestion("D O _", "🐶", "G", "B", "G", "T", "P"),
            letterQuestion("_ I S H", "🐟", "F", "S", "F", "B", "D"),
            letterQuestion("S _ N", "☀️", "U", "A", "O", "U", "E"),
            letterQuestion("B _ L L", "⚽", "A", "E", "A", "I", "O"),
            letterQuestion("B I _ D", "🐦", "R", "N", "R", "L", "T"),
        ),
    ),
    QuizSection(
        id = "animals",
        title = "Animals",
        icon = "🦁",
        questions = listOf(
            pictureQuestion("Which animal is useful in the desert?", "Camel", "Camel" to "🐫", "Elephant" to "🐘", "Monkey" to "🐒", "Donkey" to "🫏"),
            pictureQuestion("Which one is an insect?", "Cockroach", "Snake" to "🐍", "Cockroach" to "🪳", "Dog" to "🐕", "Duck" to "🦆"),
            pictureQuestion("Which one is a bird?", "Swan", "Fly" to "🪰", "Swan" to "🦢", "Cricket" to "🦗", "Butterfly" to "🦋"),
            pictureQuestion("Which animal crawls without legs?", "Snake", "Fly" to "🪰", "Zebra" to "🦓", "Snake" to "🐍", "Frog" to "🐸"),
            pictureQuestion("Which animal catches rats?", "Cat", "Cow" to "🐄", "Dog" to "🐕", "Crow" to "🐦‍⬛", "Cat" to "🐈"),
        ),
    ),
    QuizSection(
        id = "birds",
        title = "Birds",
        icon = "🦜",
        questions = listOf(
            pictureQuestion("Which is the fastest bird on land?", "Ostrich", "Ostrich" to "🪿", "Kiwi" to "🐦", "Pigeon" to "🕊️", "Peacock" to "🦚", tip = "An ostrich can run very fast."),
            pictureQuestion("Which bird says hoot, hoot?", "Owl", "Pigeon" to "🕊️", "Duck" to "🦆", "Hen" to "🐔", "Owl" to "🦉"),
            pictureQuestion("Which bird can copy human speech?", "Parrot", "Parrot" to "🦜", "Rooster" to "🐓", "Crow" to "🐦‍⬛", "Eagle" to "🦅"),
            pictureQuestion("Which bird can swim in water?", "Swan", "Parrot" to "🦜", "Owl" to "🦉", "Swan" to "🦢", "Sparrow" to "🐦"),
            pictureQuestion("Which bird has a colourful fan-like tail?", "Peacock", "Hen" to "🐔", "Kiwi" to "🐦", "Peacock" to "🦚", "Ostrich" to "🪿"),
        ),
    ),
    QuizSection(
        id = "pets",
        title = "Pet Animals",
        icon = "🐶",
        questions = listOf(
            pictureQuestion("Which one is a pet animal?", "Dog", "Bull" to "🐂", "Dog" to "🐕", "Tiger" to "🐅", "Hen" to "🐔"),
            pictureQuestion("Which one is not usually a pet?", "Tiger", "Dog" to "🐕", "Cat" to "🐈", "Tiger" to "🐅", "Parrot" to "🦜"),
            pictureQuestion("Which animal guards our house?", "Dog", "Lion" to "🦁", "Rabbit" to "🐇", "Cat" to "🐈", "Dog" to "🐕"),
            pictureQuestion("Which animal says meow?", "Cat", "Pigeon" to "🕊️", "Mosquito" to "🦟", "Cat" to "🐈", "Duck" to "🦆"),
            pictureQuestion("Which animal likes to eat carrots?", "Rabbit", "Rabbit" to "🐇", "Parrot" to "🦜", "Pigeon" to "🕊️", "Cat" to "🐈"),
        ),
    ),
    QuizSection(
        id = "fruit_veg",
        title = "Fruits & Vegetables",
        icon = "🍎",
        questions = listOf(
            pictureQuestion("Choose a red fruit.", "Apple", "Grapes" to "🍇", "Apple" to "🍎", "Banana" to "🍌", "Lemon" to "🍋"),
            pictureQuestion("Which fruit grows in a bunch?", "Grapes", "Watermelon" to "🍉", "Papaya" to "🍈", "Grapes" to "🍇", "Pineapple" to "🍍"),
            pictureQuestion("Which fruit has many seeds?", "Watermelon", "Pineapple" to "🍍", "Mango" to "🥭", "Apple" to "🍎", "Watermelon" to "🍉"),
            pictureQuestion("Which vegetable grows under the soil?", "Ginger", "Tomato" to "🍅", "Cucumber" to "🥒", "Ginger" to "🫚", "Broccoli" to "🥦"),
            pictureQuestion("Which one is hot and spicy?", "Chilli", "Pumpkin" to "🎃", "Tomato" to "🍅", "Chilli" to "🌶️", "Orange" to "🍊"),
            pictureQuestion("Which one is a flower vegetable?", "Cauliflower", "Cabbage" to "🥬", "Cauliflower" to "🥦", "Pepper" to "🫑", "Onion" to "🧅"),
        ),
    ),
    QuizSection(
        id = "babies",
        title = "Animal Families",
        icon = "🐣",
        questions = listOf(
            pictureQuestion("Choose the baby of a cat.", "Kitten", "Cub" to "🦁", "Chick" to "🐥", "Kitten" to "🐈", "Calf" to "🐄"),
            pictureQuestion("Choose the baby of a dog.", "Puppy", "Kitten" to "🐈", "Puppy" to "🐶", "Calf" to "🐄", "Chick" to "🐥"),
            pictureQuestion("Choose the baby of a cow.", "Calf", "Cub" to "🦁", "Puppy" to "🐶", "Calf" to "🐄", "Chick" to "🐥"),
            pictureQuestion("Choose the baby of a hen.", "Chick", "Chick" to "🐥", "Kitten" to "🐈", "Cub" to "🦁", "Puppy" to "🐶"),
            pictureQuestion("Choose the baby of a lion.", "Cub", "Calf" to "🐄", "Cub" to "🦁", "Chick" to "🐥", "Kitten" to "🐈"),
        ),
    ),
    QuizSection(
        id = "homes_sounds",
        title = "Homes & Sounds",
        icon = "🏡",
        questions = listOf(
            textQuestion("A lion lives in a...", "Den", "Nest", "Stable", "Den", "Kennel", hero = "🦁"),
            textQuestion("A dog lives in a...", "Kennel", "Web", "Kennel", "Nest", "Burrow", hero = "🐕"),
            textQuestion("A bird lives in a...", "Nest", "Shed", "Den", "Nest", "Stable", hero = "🐦"),
            textQuestion("A horse says...", "Neigh", "Hiss", "Neigh", "Bark", "Roar", hero = "🐴"),
            textQuestion("An elephant says...", "Trumpet", "Chirp", "Bleat", "Trumpet", "Chatter", hero = "🐘"),
        ),
    ),
    QuizSection(
        id = "food",
        title = "Food & Drinks",
        icon = "🥛",
        questions = listOf(
            textQuestion("The food we eat in the morning is...", "Breakfast", "Lunch", "Dinner", "Snacks", "Breakfast", hero = "🌅"),
            textQuestion("Grape juice is a...", "Sweet drink", "Hot drink", "Medicine", "Sweet drink", "Soup", hero = "🍇"),
            textQuestion("Jam tastes...", "Sweet", "Hot", "Sour", "Bitter", "Sweet", hero = "🍓"),
            textQuestion("The colour of milk is...", "White", "Blue", "Black", "White", "Green", hero = "🥛"),
            textQuestion("Butter is made from...", "Milk", "Rice", "Milk", "Meat", "Salt", hero = "🧈"),
        ),
    ),
    QuizSection(
        id = "transport",
        title = "Transport",
        icon = "🚂",
        questions = listOf(
            pictureQuestion("Which one sails on the sea?", "Ship", "Bus" to "🚌", "Bicycle" to "🚲", "Car" to "🚗", "Ship" to "🚢"),
            pictureQuestion("Which vehicle helps a farmer plough?", "Tractor", "Auto" to "🛺", "Motorbike" to "🏍️", "Tractor" to "🚜", "Bicycle" to "🚲"),
            pictureQuestion("Which vehicle has three wheels?", "Auto", "Ship" to "🚢", "Auto" to "🛺", "Bus" to "🚌", "Train" to "🚆"),
            pictureQuestion("Which vehicle moves on rails?", "Train", "Train" to "🚆", "Plane" to "✈️", "Boat" to "🚤", "Bicycle" to "🚲"),
            pictureQuestion("Which one does not fly in the air?", "Car", "Helicopter" to "🚁", "Plane" to "✈️", "Rocket" to "🚀", "Car" to "🚗"),
        ),
    ),
    QuizSection(
        id = "house_helpers",
        title = "House & Helpers",
        icon = "👩‍⚕️",
        questions = listOf(
            pictureQuestion("Who brings us milk?", "Milkman", "Police officer" to "👮", "Barber" to "💈", "Milkman" to "🥛", "Cleaner" to "🧹"),
            pictureQuestion("Who looks after sick people?", "Nurse", "Nurse" to "👩‍⚕️", "Mason" to "🧱", "Police officer" to "👮", "Postman" to "📮"),
            pictureQuestion("Who stitches our clothes?", "Tailor", "Gardener" to "🧑‍🌾", "Tailor" to "🧵", "Doctor" to "🩺", "Soldier" to "🪖"),
            textQuestion("We sleep in the...", "Bedroom", "Kitchen", "Bedroom", "Bathroom", "Garden", hero = "🛏️"),
            textQuestion("We take a bath in the...", "Bathroom", "Living room", "Bedroom", "Bathroom", "Kitchen", hero = "🚿"),
        ),
    ),
    QuizSection(
        id = "actions_body",
        title = "Actions & My Body",
        icon = "🏃",
        questions = listOf(
            pictureQuestion("Which picture shows skipping?", "Skipping", "Running" to "🏃", "Skipping" to "🤸", "Standing" to "🧍", "Sitting" to "🧘"),
            pictureQuestion("Which picture shows reading?", "Reading", "Crying" to "😭", "Reading" to "📖", "Sleeping" to "😴", "Clapping" to "👏"),
            textQuestion("We see with our...", "Eyes", "Ears", "Eyes", "Nose", "Hands", hero = "👀"),
            textQuestion("We smell with our...", "Nose", "Mouth", "Nose", "Feet", "Eyes", hero = "👃"),
            textQuestion("We run with our...", "Legs", "Arms", "Ears", "Legs", "Hair", hero = "🦵"),
        ),
    ),
    QuizSection(
        id = "colours_shapes",
        title = "Colours & Shapes",
        icon = "🔷",
        questions = listOf(
            pictureQuestion("What is the shape of a chess board?", "Square", "Circle" to "●", "Triangle" to "▲", "Oval" to "⬭", "Square" to "■"),
            pictureQuestion("What is the shape of an egg?", "Oval", "Star" to "★", "Circle" to "●", "Oval" to "⬭", "Square" to "■"),
            pictureQuestion("What is the shape of a bicycle tyre?", "Circle", "Circle" to "●", "Square" to "■", "Triangle" to "▲", "Oval" to "⬭"),
            textQuestion("What colour is a healthy leaf?", "Green", "Red", "Yellow", "Green", "Black", hero = "🍃"),
            textQuestion("What colour is a ripe banana?", "Yellow", "Blue", "Yellow", "Purple", "Black", hero = "🍌"),
        ),
    ),
    QuizSection(
        id = "maths",
        title = "Patterns & Maths",
        icon = "🔢",
        questions = listOf(
            textQuestion("What comes next? 1, 2, 3, 4, ...", "5", "3", "5", "6", "8", hero = "🔢"),
            textQuestion("What comes after 24?", "25", "23", "25", "24", "20", hero = "24 ➡️"),
            textQuestion("What comes before 37?", "36", "38", "73", "36", "35", hero = "⬅️ 37"),
            textQuestion("How many days are there in a week?", "7", "5", "7", "10", "12", hero = "📅"),
            textQuestion("Choose the biggest number.", "98", "55", "26", "98", "41", hero = "🍎"),
            textQuestion("What comes next? ●, ●●, ●●●, ...", "●●●●", "●●", "●●●●", "●", "●●●●●", hero = "✨"),
        ),
    ),
    QuizSection(
        id = "words",
        title = "Make New Words",
        icon = "🧩",
        questions = listOf(
            textQuestion("SUN plus FLOWER makes...", "SUNFLOWER", "SUNLIGHT", "FLOWERPOT", "SUNFLOWER", "STARFISH", hero = "☀️ + 🌻"),
            textQuestion("FOOT plus BALL makes...", "FOOTBALL", "FOOTBALL", "BASKETBALL", "FOOTPATH", "BASEBALL", hero = "🦶 + ⚽"),
            textQuestion("RAIN plus COAT makes...", "RAINCOAT", "RAINBOW", "RAINCOAT", "COATROOM", "WATERCOAT", hero = "🌧️ + 🧥"),
            textQuestion("STAR plus FISH makes...", "STARFISH", "FISHBOWL", "SUNFISH", "STARFISH", "FISHNET", hero = "⭐ + 🐟"),
            textQuestion("PEA plus COCK makes...", "PEACOCK", "PEACOCK", "COCKTAIL", "PEANUT", "SEAGULL", hero = "🫛 + 🐓"),
        ),
    ),
    QuizSection(
        id = "model_quiz",
        title = "Model Quiz",
        icon = "🏆",
        questions = listOf(
            pictureQuestion("Which picture shows skipping?", "Skipping", "Running" to "🏃", "Skipping" to "🤸", "Standing" to "🧍", "Sitting" to "🧘"),
            pictureQuestion("Which one is an insect?", "Cockroach", "Snake" to "🐍", "Cockroach" to "🪳", "Dog" to "🐕", "Duck" to "🦆"),
            pictureQuestion("Which fruit has no visible seeds inside?", "Pineapple", "Watermelon" to "🍉", "Mango" to "🥭", "Pineapple" to "🍍", "Grapes" to "🍇"),
            pictureQuestion("Which one is hot and spicy?", "Chilli", "Pumpkin" to "🎃", "Tomato" to "🍅", "Chilli" to "🌶️", "Orange" to "🍊"),
            pictureQuestion("Which is the fastest bird on land?", "Ostrich", "Ostrich" to "🪿", "Kiwi" to "🐦", "Pigeon" to "🕊️", "Peacock" to "🦚"),
            textQuestion("Breakfast is known as brain food. Yes or no?", "Yes", "Yes", "No", "Maybe", "I don't know", hero = "🥣"),
        ),
    ),
)
