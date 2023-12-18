import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MusicPlayer extends JFrame implements ActionListener {

	private JLabel currentSongLabel;
	private JLabel currentTimeLabel;
	private JLabel totalTimeLabel;

	private JPanel buttonPanel;
	private JPanel currentSongPanel;

	private JButton shuffleButton;
	private JButton previousButton;
	private JButton playButton;
	private JButton nextButton;
	private JButton loopButton;

	private ImageIcon shuffleIcon;
	private ImageIcon previousIcon;
	private ImageIcon playIcon;
	private ImageIcon pauseIcon;
	private ImageIcon nextIcon;
	private ImageIcon loopIcon;
	private ImageIcon unloopIcon;
	private ImageIcon unShuffleIcon;

	private JProgressBar progressBar;
	private JPanel borderProgressBar;

	private JFileChooser fileChooser;
	private ArrayList<File> songFileList;
	private JList<String> songList;
	private AudioPlayer audioPlayer;
	private boolean isPaused;
	private boolean isLooping;
	private boolean isShuffle;

	private int songFilePos;
	private int numSongs;
	private int currentlyPlayingIndex = -1;
	private JLabel imageLabel;
	private JLabel titleLabel;
	private JLabel artistLabel;

	private JMenuItem openPlaylistMenuItem;

	public class DatabaseConnection {
		private static final String URL = "jdbc:mysql://localhost:3306/sportifly";
		private static final String USER = "root";
		private static final String PASSWORD = "";

		public Connection getConnection() throws SQLException {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		}
	}

	private void loadSongsFromDatabase() {
		try (Connection connection = new DatabaseConnection().getConnection()) {
			String query = "SELECT * FROM songs";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						String filePath = resultSet.getString("file_path");
						if (filePath == null) {
							System.out.println("Path tidak ditemukan!");
						} else {
							System.out.println("Path : " + filePath);
							File songFile = new File(filePath);
							songFileList.add(songFile);
						}
					}
					numSongs = songFileList.size();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Font loadExternalFont(String fontPath, float size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		}
	}

	public MusicPlayer() {
		super("SpotiFly");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		songFileList = new ArrayList<>();
		fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));

		loadSongsFromDatabase();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		shuffleButton = new JButton();
		previousButton = new JButton();
		playButton = new JButton();
		nextButton = new JButton();
		loopButton = new JButton();

		shuffleButton.addActionListener(this);
		previousButton.addActionListener(this);
		playButton.addActionListener(this);
		nextButton.addActionListener(this);
		loopButton.addActionListener(this);
		audioPlayer = new AudioPlayer();

		shuffleIcon = new ImageIcon("pngs/shuffle.png");
		previousIcon = new ImageIcon("pngs/previous.png");
		playIcon = new ImageIcon("pngs/play.png");
		pauseIcon = new ImageIcon("pngs/pause.png");
		nextIcon = new ImageIcon("pngs/next.png");
		loopIcon = new ImageIcon("pngs/loop.png");
		unloopIcon = new ImageIcon("pngs/unloop.png");
		unShuffleIcon = new ImageIcon("pngs/unshuffle.png");

		shuffleButton.setIcon(shuffleIcon);
		previousButton.setIcon(previousIcon);
		playButton.setIcon(playIcon);
		nextButton.setIcon(nextIcon);
		loopButton.setIcon(loopIcon);

		shuffleButton.setContentAreaFilled(false);
		previousButton.setContentAreaFilled(false);
		playButton.setContentAreaFilled(false);
		nextButton.setContentAreaFilled(false);
		loopButton.setContentAreaFilled(false);

		shuffleButton.setBorderPainted(false);
		previousButton.setBorderPainted(false);
		playButton.setBorderPainted(false);
		nextButton.setBorderPainted(false);
		loopButton.setBorderPainted(false);

		buttonPanel = new JPanel();
		buttonPanel.setBounds(-10, 720, 500, 80);
		buttonPanel.setBackground(new Color(0x191414));

		buttonPanel.add(shuffleButton);
		buttonPanel.add(previousButton);
		buttonPanel.add(playButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(loopButton);

		add(buttonPanel);

		songList = new JList<>();
		songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		songList.setForeground(new Color(0x1DB954));
		songList.setBackground(new Color(0x191414));
		songList.setFont(new Font("", Font.BOLD, 15));

		songList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedIdx = songList.getSelectedIndex();
					if (selectedIdx != -1) {
						audioPlayer.stop();
						songFilePos = selectedIdx;
						loadAndPlayNewSong();
						updateSelectedSongAppearance(selectedIdx);

						String songFileName = songFileList.get(songFilePos).getName();
						String songTitle = songFileName.substring(0, songFileName.lastIndexOf('.'));
						titleLabel.setText(songTitle);

						String songArtist = getSongArtistFromDatabase(songFileList.get(songFilePos).getPath());
						artistLabel.setText(songArtist);
					}
				}
			}
		});

		JLabel logoLabel = new JLabel();
		logoLabel.setBounds(140, 50, 256, 75);
		ImageIcon originalIcon = new ImageIcon("pngs/logo.png");
		Image originalImage = originalIcon.getImage();
		Image resizedImage = originalImage.getScaledInstance(206, 60, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);
		logoLabel.setIcon(resizedIcon);
		add(logoLabel);

		imageLabel = new JLabel();
		imageLabel.setBounds(50, 150, 400, 400);
		add(imageLabel);

		currentSongPanel = new JPanel();
		currentSongPanel.setBounds(50, 655, 400, 30);
		currentSongPanel.setLayout(new FlowLayout());
		currentSongPanel.setOpaque(false);
		currentSongPanel.setForeground(new Color(0x1DB954));
		currentSongLabel = new JLabel("");
		currentSongPanel.add(currentSongLabel);
		add(currentSongPanel);

		borderProgressBar = new JPanel();
		borderProgressBar.setBounds(50, 698, 400, 5);
		borderProgressBar.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		borderProgressBar.setBackground(new Color(0x191414));
		add(borderProgressBar);

		progressBar = new JProgressBar();
		progressBar.setBounds(50, 698, 400, 5);
		progressBar.setForeground(Color.WHITE);
		progressBar.setBackground(new Color(0x191414));

		add(progressBar);

		currentTimeLabel = new JLabel();
		currentTimeLabel.setBounds(50, 705, 50, 20);
		currentTimeLabel.setText("0:00");
		add(currentTimeLabel);

		totalTimeLabel = new JLabel();
		totalTimeLabel.setBounds(428, 705, 50, 20);
		totalTimeLabel.setText("0:00");
		add(totalTimeLabel);

		titleLabel = new JLabel("");
		titleLabel.setBounds(50, 610, 400, 50);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(loadExternalFont("Font/GothamBold.ttf", 25f));
		add(titleLabel);

		artistLabel = new JLabel("");
		artistLabel.setBounds(50, 660, 400, 30);
		artistLabel.setForeground(Color.WHITE);
		artistLabel.setFont(loadExternalFont("Font/GothamLight.ttf", 14f));
		add(artistLabel);

		getContentPane().setBackground(new Color(0x191414));
		setSize(500, 890);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		File firstSong = new File("Music/1.wav");
		songFileList.add(firstSong);
		numSongs = songFileList.size();

		try {
			audioPlayer.load(firstSong.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Font externalFont = loadExternalFont("Font\\GothamBold.ttf", 16f);
		setUIFont(externalFont);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		openPlaylistMenuItem = new JMenuItem("Open Playlist");
		openPlaylistMenuItem.addActionListener(this);
		fileMenu.add(openPlaylistMenuItem);

		updateSelectedSongAppearance(songFilePos);
	}

	public void actionPerformed(ActionEvent event) {
		System.out.println("Action Performed: " + event.getActionCommand());
		if (event.getSource() == playButton) {
			togglePlay(0);
		}
		if (event.getSource() == previousButton) {
			previous();
		}
		if (event.getSource() == nextButton) {
			next();
		}
		if (event.getSource() == loopButton) {
			toggleLoop();
		}
		if (event.getSource() == shuffleButton) {
			shuffle();
		}
		if (event.getSource() == openPlaylistMenuItem) {
			showPlaylistPopup();
		}
	}

	private void showPlaylistPopup() {
		String[] songNames = new String[numSongs];
		for (int i = 0; i < numSongs; i++) {
			songNames[i] = getSongTitleFromDatabase(songFileList.get(i).getPath());
		}

		String selectedSong = (String) JOptionPane.showInputDialog(
				this,
				"Choose a song:",
				"Open Playlist",
				JOptionPane.QUESTION_MESSAGE,
				null,
				songNames,
				songNames[0]);

		int selectedIndex = -1;
		for (int i = 0; i < numSongs; i++) {
			String songTitle = getSongTitleFromDatabase(songFileList.get(i).getPath());
			if (songTitle.equals(selectedSong)) {
				selectedIndex = i;
				break;
			}
		}

		if (selectedIndex != -1) {
			audioPlayer.stop();
			songFilePos = selectedIndex;
			loadAndPlayNewSong();
			updateSelectedSongAppearance(selectedIndex);

			String songFilePath = songFileList.get(songFilePos).getPath();
			String songTitle = getSongTitleFromDatabase(songFilePath);
			String songArtist = getSongArtistFromDatabase(songFilePath);

			titleLabel.setText(songTitle);
			artistLabel.setText(songArtist);
		}
	}

	private String getSongTitleFromDatabase(String filePath) {
		String title = "";
		try (Connection connection = new DatabaseConnection().getConnection()) {
			String query = "SELECT title FROM songs WHERE file_path = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, filePath.replace("\\", "/"));

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						title = resultSet.getString("title");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return title;
	}

	private void updateSelectedSongAppearance(int selectedIdx) {
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setBackground(new Color(0x191414));
		renderer.setForeground(Color.WHITE);

		songList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
			Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (isSelected) {
				c.setBackground(Color.BLACK);
				c.setForeground(Color.WHITE);
			} else {
				c.setBackground(new Color(0x191414));
				c.setForeground(new Color(0x1DB954));
			}
			return c;
		});
	}

	private void setTitleLabelFont(Font font) {
		titleLabel = new JLabel("");
		titleLabel.setBounds(50, 630, 400, 50);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(font);
		add(titleLabel);
	}

	private void setArtistLabelFont(Font font) {
		artistLabel = new JLabel("");
		artistLabel.setBounds(50, 660, 400, 30);
		artistLabel.setForeground(Color.WHITE);
		artistLabel.setFont(font);
		add(artistLabel);
	}

	private void togglePlay(int i) {
		System.out.println("Toggle Play: " + i);
		System.out.println("Is Paused: " + isPaused);
		if (audioPlayer != null && audioPlayer.getClip() != null) {
			if (audioPlayer.isRunning()) {
				audioPlayer.stop();
				isPaused = true;
				playButton.setIcon(playIcon);
			} else {
				if (!isPaused) {
					if (audioPlayer.getClip() != null) {
						if (isShuffle) {
							songFilePos = i;
						}
						loadAndPlayNewSong();
						startTimer();
						playButton.setIcon(pauseIcon);
						currentlyPlayingIndex = i;
						isPaused = false;
					}
				} else {
					audioPlayer.resume();
					playButton.setIcon(pauseIcon);
				}
			}

			if (!isPaused) {
				updateSelectedSongAppearance(i);
			} else {
				i = currentlyPlayingIndex;
			}

			System.out.println("Last Valid Position: " + audioPlayer.getLastValidPosition());
			System.out.println("Is Paused: " + isPaused);
		}
	}

	private void next() {
		audioPlayer.stop();
		if (!isShuffle) {
			playRandomSong();
		} else {
			playNextSong();
		}
	}

	private void playNextSong() {
		songFilePos++;
		if (songFilePos == numSongs) {
			songFilePos = 0;
		}
		loadAndPlayNewSong();

		if (isLooping) {
			if (audioPlayer.getClip().getMicrosecondPosition() >= audioPlayer.getClip().getMicrosecondLength()) {
				audioPlayer.playFromPosition(0);
			}
		}

		songList.setSelectedIndex(songFilePos);
		songList.ensureIndexIsVisible(songFilePos);
		System.out.println("Playing Next Song: " + songFileList.get(songFilePos).getName());
	}

	private void playRandomSong() {
		Random rand = new Random();
		int randomIndex;
		do {
			randomIndex = rand.nextInt(numSongs);
		} while (randomIndex == songFilePos);
		songFilePos = randomIndex;
		loadAndPlayNewSong();

		if (isLooping) {
			audioPlayer.playFromPosition(0);
		}

		songList.setSelectedIndex(songFilePos);
		songList.ensureIndexIsVisible(songFilePos);
		System.out.println("Next Song: " + songFileList.get(songFilePos).getName());
	}

	private void previous() {
		audioPlayer.stop();
		if (!isShuffle) {
			playRandomPreviousSong();
		} else {
			playPreviousSong();
		}
	}

	private void playPreviousSong() {
		songFilePos--;
		if (songFilePos < 0) {
			songFilePos = numSongs - 1;
		}
		loadAndPlayNewSong();

		if (isLooping) {
			if (audioPlayer.getClip().getMicrosecondPosition() >= audioPlayer.getClip().getMicrosecondLength()) {
				audioPlayer.playFromPosition(0);
			}
		}

		songList.setSelectedIndex(songFilePos);
		songList.ensureIndexIsVisible(songFilePos);
		System.out.println("Playing Previous Song: " + songFileList.get(songFilePos).getName());
	}

	private void playRandomPreviousSong() {
		Random rand = new Random();
		int randomIndex;
		do {
			randomIndex = rand.nextInt(numSongs);
		} while (randomIndex == songFilePos);
		songFilePos = randomIndex;
		loadAndPlayNewSong();

		if (isLooping) {
			audioPlayer.playFromPosition(0);
		}

		songList.setSelectedIndex(songFilePos);
		songList.ensureIndexIsVisible(songFilePos);
		System.out.println("Previous Song: " + songFileList.get(songFilePos).getName());
	}

	private void toggleLoop() {
		isLooping = !isLooping;
		audioPlayer.setLoop(isLooping);
		loopButton.setIcon(isLooping ? loopIcon : unloopIcon);

		System.out.println("Toggle Looping: " + isLooping);
	}

	private void shuffle() {
		isShuffle = !isShuffle;
		audioPlayer.stop();
		Random rand = new Random();
		shuffleButton.setIcon(isShuffle ? unShuffleIcon : shuffleIcon);
		songFilePos = rand.nextInt(numSongs);
		currentlyPlayingIndex = songFilePos;
		loadAndPlayNewSong();

		System.out.println("Toggle Shuffle: " + isShuffle);
		System.out.println("Shuffled Song: " + songFileList.get(songFilePos).getName());
	}

	private void loadAndPlayNewSong() {
		try {
			long position = 0;
			if (!isShuffle) {
				position = 0;
			}

			String filePath = songFileList.get(songFilePos).getPath();
			audioPlayer.load(filePath);
			audioPlayer.setLoop(isLooping);
			audioPlayer.playFromPosition(position);
			isPaused = false;
			playButton.setIcon(pauseIcon);
			updateSelectedSongAppearance(songFilePos);

			String songTitle = getSongTitleFromDatabase(filePath);
			String songArtist = getSongArtistFromDatabase(filePath);

			titleLabel.setText(songTitle);
			artistLabel.setText(songArtist);

			audioPlayer.addCompletionListener(() -> {
				if (!isLooping) {
					playNextSong();
				}
			});

			loadImageForCurrentSong();

			displayProgress();
			startTimer();
			System.out.println("Loading and Playing New Song: " + songFileList.get(songFilePos).getName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getSongArtistFromDatabase(String filePath) {
		String artist = "";
		try (Connection connection = new DatabaseConnection().getConnection()) {
			String query = "SELECT artist FROM songs WHERE file_path = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, filePath.replace("\\", "/"));

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						artist = resultSet.getString("artist");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artist;
	}

	private void loadImageForCurrentSong() {
		try (Connection connection = new DatabaseConnection().getConnection()) {
			String query = "SELECT thumbnail FROM songs WHERE file_path = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, songFileList.get(songFilePos).getPath().replace("\\", "/"));

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						String thumbnailPath = resultSet.getString("thumbnail");
						if (thumbnailPath == null) {
							System.err.println("Thumbnail path not found in the database for path: "
									+ songFileList.get(songFilePos).getPath());
						} else {
							System.out.println("Thumbnail Path: " + thumbnailPath);

							ImageIcon newImageIcon = new ImageIcon(getClass().getResource(thumbnailPath));

							if (newImageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
								int originalWidth = newImageIcon.getIconWidth();
								int originalHeight = newImageIcon.getIconHeight();

								double widthScale = (double) originalWidth / 400;
								double heightScale = (double) originalHeight / 400;

								double scale = Math.min(widthScale, heightScale);

								int scaledWidth = (int) (originalWidth / scale);
								int scaledHeight = (int) (originalHeight / scale);

								Image scaledImage = newImageIcon.getImage().getScaledInstance(scaledWidth, scaledHeight,
										Image.SCALE_SMOOTH);

								if (imageLabel != null) {
									imageLabel.setIcon(new ImageIcon(scaledImage));
								} else {
									System.err.println("imageLabel is null!");
								}
							} else {
								System.err.println("Failed to load thumbnail. Image load status: "
										+ newImageIcon.getImageLoadStatus());
							}
						}
					} else {
						System.err.println(
								"Thumbnail data not found for path: " + songFileList.get(songFilePos).getPath());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startTimer() {
		System.out.println("Starting Timer...");

		progressBar.setMaximum((int) audioPlayer.getClip().getMicrosecondLength());
		progressBar.setValue((int) audioPlayer.getClip().getMicrosecondPosition());

		totalTimeLabel.setText(String.format("%d:%02d",
				audioPlayer.getClip().getMicrosecondLength() / 1000000 / 60,
				audioPlayer.getClip().getMicrosecondLength() / 1000000 % 60));

		java.util.Timer timer = new java.util.Timer();
		TimerTaskRunner task = new TimerTaskRunner(() -> {
			progressBar.setValue((int) audioPlayer.getClip().getMicrosecondPosition());
			currentTimeLabel.setText(String.format("%d:%02d",
					audioPlayer.getClip().getMicrosecondPosition() / 1000000 / 60,
					audioPlayer.getClip().getMicrosecondPosition() / 1000000 % 60));
			if (audioPlayer.getClip().getMicrosecondPosition() >= audioPlayer.getClip().getMicrosecondLength()) {
				if (isLooping) {
					audioPlayer.playFromPosition(0);
				} else {
					timer.cancel();
					playNextSong();
				}
			}
		});
		System.out.println("Current Microsecond Position: " + audioPlayer.getClip().getMicrosecondPosition());
		System.out.println("Clip Length: " + audioPlayer.getClip().getMicrosecondLength());
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	private void displayProgress() {
		progressBar.setMaximum((int) audioPlayer.getClip().getMicrosecondLength());
		progressBar.setValue((int) audioPlayer.getClip().getMicrosecondPosition());

		totalTimeLabel.setText(String.format("%d:%02d",
				audioPlayer.getClip().getMicrosecondLength() / 1000000 / 60,
				audioPlayer.getClip().getMicrosecondLength() / 1000000 % 60));

		java.util.Timer timer = new java.util.Timer();
		TimerTaskRunner task = new TimerTaskRunner(() -> {
			progressBar.setValue((int) audioPlayer.getClip().getMicrosecondPosition());
			currentTimeLabel.setText(String.format("%d:%02d",
					audioPlayer.getClip().getMicrosecondPosition() / 1000000 / 60,
					audioPlayer.getClip().getMicrosecondPosition() / 1000000 % 60));
		});
		timer.scheduleAtFixedRate(task, 0, 1000);

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void setUIFont(Font font) {
		try {
			titleLabel.setFont(font);
			artistLabel.setFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MusicPlayer musicPlayer = new MusicPlayer();
			try {
				Font titleFont = musicPlayer.loadExternalFont("Font/GothamBold.ttf", 25f);
				Font artistFont = musicPlayer.loadExternalFont("Font/GothamMedium.ttf", 14f);

				musicPlayer.setTitleLabelFont(titleFont);
				musicPlayer.setArtistLabelFont(artistFont);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}