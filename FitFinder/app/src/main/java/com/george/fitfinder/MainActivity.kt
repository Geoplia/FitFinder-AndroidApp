package com.george.fitfinder

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import java.time.format.TextStyle
import kotlin.system.exitProcess

//Main Activity NavigationCreation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            FitFinderApp(navController)
        }
    }
}

//NavigationHandler
@Composable
fun FitFinderApp(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        NavHost(navController = navController as NavHostController, startDestination = "main") {
            composable("main") { MainScreen(navController = navController) }
            composable("settings") { SettingsPage(navController = navController) }
            composable("profile") { ProfilePage(navController = navController) }
            composable("edit_page") { EditPage(navController = navController) }
            composable("find_near") { FindNear(navController = navController) }
            composable("home_gym") { HomeGymScreen(navController = navController) }
            composable("left_page_route") { HistoryScreen(navController = navController) }
            composable("right_page_route") { AchievementsScreen(navController = navController) }

        // Define other destinations here
        }
    }
}

//Main Screen
@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopBar(navController = navController)

        // Background Carousel of Photos
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray) // Replace with the actual background color of your carousel
        ) {
            PhotoCarousel()

            // Columns displayed on top of the carousel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Row containing two clickable columns
                Row(
                    modifier = Modifier.fillMaxSize()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClickableColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                            onClick = {
                            // Handle click action for the left column
                            navController.navigate("left_page_route")
                        }
                    ) {
                        // Content for the left column
                        Text(text = "Left Column Content", color = colorResource(R.color.accentColor))
                    }

                    ClickableColumn(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            // Handle click action for the right column
                            navController.navigate("right_page_route")
                        }
                    ) {
                        // Content for the right column
                        Text(text = "Right Column Content", color = colorResource(R.color.accentColor))
                    }
                }
            }
        }

        // Bottom Navigation
        BottomNavigation(navController = navController)
    }
}

//Carousel Function
@Composable
fun PhotoCarousel() {
    val images = listOf(
        R.drawable.braden_collum_9hi8ujmsdza_unsplash,
        R.drawable.connor_coyne_ogqwlzwrsai_unsplash,
        R.drawable.marcus_ng_zbbhkq0m2am_unsplash,
        R.drawable.markus_spiske_bfphccvhl6e_unsplash,
        R.drawable.victor_freitas_wvdydxdzkhs_unsplash
        // Add more images as needed
    )
    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = currentIndex) {
        coroutineScope.launch {
            while (true) {
                delay(7000) // Wait for 7 seconds
                currentIndex = (currentIndex + 1) % images.size
            }
        }
    }

    Image(
        painter = rememberImagePainter(images[currentIndex]),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds // Adjust content scale to fill the bounds
    )
}

//Clickable Column
@Composable
fun ClickableColumn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(Color.White, RoundedCornerShape(16.dp))
            .background(color = colorResource(R.color.darkBlue))// Add padding to the content inside the column
    ) {
        content() // Include the content inside the column
    }
}

//Settings Page
@Composable
fun SettingsPage(navController: NavController) {
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue)), // Set background color to dark blue
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clickable { navController.popBackStack() }
                .background(colorResource(R.color.darkBlue))
                .align(AbsoluteAlignment.Left)

        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Back",
                modifier = Modifier.size(50.dp),
                tint = Color.White
            )
        }

        Text(
            text = "Settings",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .background(colorResource(R.color.darkBlue)), // Set background color to dark blue
            color = Color.White
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add padding to the Surface
            color = colorResource(R.color.darkBlue),
            //elevation = 8.dp // Add elevation to the Surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(colorResource(R.color.darkBlue))
                )
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White
                )

                SettingsOption(title = "Notification", description = "Manage notification settings")
                SettingsOption(title = "Account", description = "Manage account details")
                SettingsOption(title = "Privacy", description = "Adjust privacy settings")
                SettingsOption(title = "Appearance", description = "Customize app appearance")
                SettingsOption(title = "Security", description = "Enhance account security")

                LogOutButton(
                    text = "Delete Account",
                    backgroundColor = colorResource(R.color.lightGray)
                ) {
                    // Handle delete account button click
                }

                LogOutButton(
                    text = "Log Out",
                    backgroundColor = colorResource(R.color.lightGray)
                ) {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            }
        }
    }
}
@Composable
fun SettingsOption(title: String, description: String, textColor: Color = Color.White) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = textColor)
        Text(text = description, style = MaterialTheme.typography.labelMedium, color = textColor)
    }
}

//Profile Page
@Composable
fun ProfilePage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clickable { navController.popBackStack() }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Back",
                modifier = Modifier.size(50.dp),
                tint = Color.Black // Adjust color as needed
            )
        }

        // Add some space between back button and profile picture
        Spacer(modifier = Modifier.height(16.dp))

        // Profile picture
        Image(
            painter = painterResource(id = R.drawable.profile_pic), // Replace with your image resource
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(180.dp) // Increase the size of the profile picture
                .clip(shape = CircleShape)
                .border(4.dp, Color.Black, CircleShape) // Increase border width
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)// Increase bottom padding
        )

        // Name of the user
        Text(
            text = "User's Name",
            style = MaterialTheme.typography.headlineLarge, // Increase text size
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
            color = colorResource(R.color.accentColor)
        )

        // Edit button
        Button(
            onClick = { navController.navigate("edit_page") }, // Navigate to edit page
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Edit", fontSize = 20.sp) // Increase button text size
        }

        // History section
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineMedium, // Increase text size
            modifier = Modifier.padding(bottom = 8.dp),
            color = colorResource(R.color.accentColor)
        )

        // Workout details
        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            // Column for workout names
            Column(
                modifier = Modifier.weight(1f), // Occupy equal space
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Workout Names",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("Push-ups",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
                Text("Squats",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
                Text("Plank",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            // Column for dates
            Column(
                modifier = Modifier.weight(1f), // Occupy equal space
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("2024-02-24",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
                Text("2024-02-25",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
                Text("2024-02-26",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
            }

            // Column for durations
            Column(
                modifier = Modifier.weight(1f), // Occupy equal space
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Duration",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("10 min", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
                Text("15 min",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
                Text("20 min",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp))
            }
        }

        // Achievements section
        Column(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Achievements",
                style = MaterialTheme.typography.headlineMedium, // Increase text size
                modifier = Modifier.padding(bottom = 8.dp),
                color = colorResource(R.color.accentColor)
            )

            Row {
                // First achievement
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Achievement 1", modifier = Modifier.size(48.dp))
                    Text("Achievement 1", style = MaterialTheme.typography.bodySmall)
                }

                // Second achievement
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Achievement 2", modifier = Modifier.size(48.dp))
                    Text("Achievement 2", style = MaterialTheme.typography.bodySmall)
                }

                // Third achievement
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Achievement 3", modifier = Modifier.size(48.dp))
                    Text("Achievement 3", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
@Composable
fun EditPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Back button
        Box(
            modifier = Modifier
                .size(56.dp)
                .clickable { navController.popBackStack() }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }

        // Add some space between back button and profile picture
        Spacer(modifier = Modifier.height(16.dp))

        // Profile picture
        Image(
            painter = painterResource(id = R.drawable.profile_pic), // Replace with your image resource
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(180.dp) // Increase the size of the profile picture
                .clip(shape = CircleShape)
                .border(4.dp, Color.White, CircleShape) // Increase border width
                .padding(bottom = 32.dp)
        )

// Mutable state for each user data field
        val firstNameState = remember { mutableStateOf("John") }
        val lastNameState = remember { mutableStateOf("Doe") }
        val dobState = remember { mutableStateOf("1990-01-01") }
        val emailState = remember { mutableStateOf("john.doe@example.com") }
        val genderState = remember { mutableStateOf("Male") }
        val heightState = remember { mutableStateOf("180 cm") }
        val weightState = remember { mutableStateOf("75 kg") }
        // User data list
        UserDataItem("First Name", firstNameState)
        UserDataItem("Last Name", lastNameState)
        UserDataItem("Date of Birth", dobState)
        UserDataItem("Email", emailState)
        UserDataItem("Gender", genderState)
        UserDataItem("Height", heightState)
        UserDataItem("Weight", weightState)
    }
}
@Composable
fun UserDataItem(label: String, valueState: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp), // Increase text size
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        // EditableText to enable text editing
        var textValue by remember { mutableStateOf(valueState.value) }
        val focusManager = LocalFocusManager.current

        TextField(
            value = textValue,
            onValueChange = {
                textValue = it // Update textValue when user types
                valueState.value = it // Update valueState immediately
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                valueState.value = textValue
                focusManager.clearFocus()
            }),
        )
    }
}

//Find Near
@Composable
fun FindNear(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clickable { navController.popBackStack() }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
        }

        // Add some space between back button and profile picture
        Spacer(modifier = Modifier.height(16.dp))

        // Text "Find Nearby"
        Text(
            text = "Find Nearby",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = colorResource(R.color.accentColor)

        )
                    // Add a button to find activity
        Button(
            onClick = { /* Handle find activity button click */ },
            modifier = Modifier
                .padding(vertical = 8.dp) // Add vertical padding
                .height(72.dp) // Set the desired height
        ) {
            Text(
                "Find Activity",
                fontSize = 20.sp, // Increase the font size
                modifier = Modifier.padding(vertical = 8.dp) // Add vertical padding to the text
            )
        }
    }
}

@Composable
fun rememberImagePainter(image: Int): Painter {
    return painterResource(id = image)
}

//HomeGym
@Composable
fun HomeGymScreen(navController: NavController) {
    val context = LocalContext.current
    val Muscles = arrayOf("Arms", "Legs", "Abs",)
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(Muscles[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue))
    ) {
        // Button to navigate to the main page
        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {
            IconButton(
                onClick = { navController.navigate("main") },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = "Back to Main")
            }
        }

        Text(
            text = "Home Gym",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            color = colorResource(R.color.accentColor)
        )
        Image(
            painter = painterResource(id = R.drawable.workout), // Replace with your image resource
            contentDescription = "Image",
            modifier = Modifier.fillMaxWidth()
        )
        // Muscle Groups Box with Dropdown Menu
       Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) /* {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Muscles.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        } */
        }
    }

//HistoryScreen
@Composable
fun HistoryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue))
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {
            IconButton(
                onClick = { navController.navigate("main") },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = "Back to Main")
            }
        }// Header/Header Bar
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 8.dp),
            color = colorResource(R.color.accentColor)
        )

        // List of Past Workouts
        WorkoutList()

        }
}
@Composable
fun WorkoutList() {
    // Dummy workout data
    val workoutData = listOf(
        WorkoutItem("Running", "2024-02-20", "30 min", "5.2 km"),
        WorkoutItem("Cycling", "2024-02-18", "45 min", "15 km"),
        WorkoutItem("Yoga", "2024-02-15", "60 min", ""),
        // Add more workout items as needed
    )

    // Display each workout item
    workoutData.forEach { workout ->
        WorkoutItemRow(workout = workout)
    }
}
@Composable
fun WorkoutItemRow(workout: WorkoutItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = workout.activityType, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(text = workout.date, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(text = workout.duration, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(text = workout.distance, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}
data class WorkoutItem(
    val activityType: String,
    val date: String,
    val duration: String,
    val distance: String
)

//Avhievements
@Composable
fun AchievementsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkBlue))
    ) {

            Box(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = { navController.navigate("main") },
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Back to Main")
                }
            }

        // Header/Header Bar
        Text(
            text = "Achievements",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 8.dp),
            color = colorResource(R.color.accentColor)
        )

        // List of Achievements
        AchievementList()


    }
}
@Composable
fun AchievementList() {
    // Dummy achievement data
    val achievementData = listOf(
        AchievementItem("First 5K Run", "Complete your first 5K run"),
        AchievementItem("Cycling Enthusiast", "Cover 50 km on your bicycle"),
        AchievementItem("Yoga Master", "Practice yoga for 30 consecutive days"),
        // Add more achievement items as needed
    )

    // Display each achievement item
    achievementData.forEach { achievement ->
        AchievementItemRow(achievement = achievement)
    }
}
@Composable
fun AchievementItemRow(achievement: AchievementItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.medal), // Placeholder icon
            contentDescription = "Achievement Icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = achievement.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = achievement.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
data class AchievementItem(
    val name: String,
    val description: String
)

//Top Bar
@Composable
fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.accentColor)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        // Settings Button
        SettingsButton(navController = navController)
        // Profile Picture
        ProfileButton(navController = navController)
        // News Button
        IconButton(onClick = { /* Handle news button click */ }) {
            Icon(Icons.Default.Notifications, contentDescription = "News")
        }
    }
}

//Buttons Functions
@Composable
fun SettingsButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("settings") }) {
        Icon(Icons.Default.Settings, contentDescription = "Settings")
    }
}
@Composable
fun ProfileButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("profile") }) {
        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
    }
}
@Composable
fun HomeGymButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("home_gym") }) {
        Icon(Icons.Default.Home, contentDescription = "To Home Gym")
    }
}
@Composable
fun LogOutButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White // Adjust text color as needed
        )
    ) {
        Text(text = text)
    }
}
@Composable
fun Find_Near_Button(navController: NavController) {
    IconButton(onClick = { navController.navigate("find_near") }) {
        Icon(Icons.Default.Search, contentDescription = "find_near")
    }
}

//BottomNavigation
@Composable
fun BottomNavigation(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.accentColor)), // Add some padding to separate it from the bottom
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Find_Near_Button(navController = navController)
        IconButton(onClick = { /* Handle search icon click */ }) {
            Icon(Icons.Default.Favorite , contentDescription = "Search")
        }
        HomeGymButton(navController = navController)
        IconButton(onClick = { /* Handle favorite icon click */ }) {
            Icon(Icons.Default.Done , contentDescription = "Favorite")
        }
        IconButton(onClick = { /* Handle account icon click */ }) {
            Icon(Icons.Default.MoreVert , contentDescription = "Account")
        }
    }
}





