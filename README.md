# ExpandableViewLibrary  [![](https://jitpack.io/v/levezf/ExpandableViewLibrary.svg)](https://jitpack.io/#levezf/ExpandableViewLibrary)

A library for creating expansive components/cards.

## Adding ExpandableViewLibrary to your build

To add a dependency on TaskLibrary using Maven, use the following:

Step 1 - Add the JitPack repository to your build file
```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Step 2 - Add the dependency
```
<dependency>
  <groupId>com.github.levezf</groupId>
  <artifactId>ExpandableViewLibrary</artifactId>
  <version>0.1.1</version>
</dependency>
```
To add a dependency using Gradle:

Step 1 - Add the JitPack repository to your build file
```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```
Step 2 - Add the dependency
```
dependencies {
    implementation 'com.github.levezf:ExpandableViewLibrary:0.1.1'
}
```
## Usage

With this library you can change colors of the components, use or not the animation and add your custom layout.

### Using View

#### Declaring the view:

Example:
```
<com.levez.expandablecard.ExpandableView
            android:id="@+id/expandableview"
            app:header_elevation="4dp"
            app:icon="@drawable/ic_android_black_24dp"
            app:title_header="Android teste Expandable"
            app:animation="true"
            app:header_clickable="true"
            app:start_expanded="false"
            app:child_layout="@layout/child"
            app:title_header_color="@android:color/white"
            app:icon_color="@android:color/white"
            app:drawable_indicator_color="@android:color/white"
            app:header_color="@color/colorAccent"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
```

The only required attributes are:
 * ```title_header```
 * ```child_layout```
 
 *Failure to use these attributes resulted in throwing exceptions of type ```ExpandableViewException```:*
 * ```TitleNotFound```
 * ```LayoutChildNotFound```

#### Setting values programmatically

Example:
```
[...]
ExpandableView expandable = findViewById(R.id.expandableview);
expandable.setTitle("This is a example");
expandable.setIconColor(R.color.azul);
expandable.setDrawableIndicatorColor(R.color.azul);
expandable.setHeaderClickable(true);
expandable.setIsAnimation(false);
expandable.setOnChangeStateListener(new ExpandableView.OnChangeStateListener() {
    @Override
    public void changeState() {
        Toast.makeText(MainActivity.this, "ChangeState", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void expanded() {
        Toast.makeText(MainActivity.this, "Expanded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void collapsed() {
        Toast.makeText(MainActivity.this, "Collapsed", Toast.LENGTH_SHORT).show();
    }
});
[...]
```
