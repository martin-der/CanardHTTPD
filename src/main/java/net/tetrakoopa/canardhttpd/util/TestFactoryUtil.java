package net.tetrakoopa.canardhttpd.util;

import net.tetrakoopa.canardhttpd.domain.EventLog;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TestFactoryUtil {



	@Deprecated
	public static void addFakeObjectsToManager(SharesManager sharesManager) {
		if (sharesManager.getThings().isEmpty()) {
			int i = 1;
			for (; i > 0; i--) {
				sharesManager.addText("Some thoughts I had...", "The phenomenology movement in philosophy saw a radical change in the way in which we understand thought. Martin Heidegger's phenomenological analyses of the existential structure of man in Being and Time cast new light on the issue of thinking, unsettling traditional cognitive or rational interpretations of man which affect the way we understand thought. The notion of the fundamental role of non-cognitive understanding in rendering possible thematic consciousness informed the discussion surrounding Artificial Intelligence during the 1970s and 1980s.[12]\n" +
						"\n" +
						"Phenomenology, however, is not the only approach to thinking in modern Western philosophy. Philosophy of mind is a branch of philosophy that studies the nature of the mind, mental events, mental functions, mental properties, consciousness and their relationship to the physical body, particularly the brain. The mind-body problem, i.e. the relationship of the mind to the body, is commonly seen as the central issue in philosophy of mind, although there are other issues concerning the nature of the mind that do not involve its relation to the physical body.[13]");
				sharesManager.addText(null, "Lalalalalalalalolololilili");
				String if_kipplig =
						"IF you can keep your head when all about you\n"
								+"Are losing theirs and blaming it on you,\n"
								+"If you can trust yourself when all men doubt you,\n"
								+"But make allowance for their doubting too;\n"
								+"If you can wait and not be tired by waiting,\n"
								+"Or being lied about, don't deal in lies,\n"
								+"Or being hated, don't give way to hating,\n"
								+"And yet don't look too good, nor talk too wise:\n"
								+"If you can dream - and not make dreams your master;\n"
								+"If you can think - and not make thoughts your aim;\n"
								+"If you can meet with Triumph and Disaster\n"
								+"And treat those two impostors just the same;\n"
								+"If you can bear to hear the truth you've spoken\n"
								+"Twisted by knaves to make a trap for fools,\n"
								+"Or watch the things you gave your life to, broken,\n"
								+"And stoop and build 'em up with worn-out tools:\n"
								+"\n"
								+"If you can make one heap of all your winnings\n"
								+"And risk it on one turn of pitch-and-toss,\n"
								+"And lose, and start again at your beginnings\n"
								+"And never breathe a word about your loss;\n"
								+"If you can force your heart and nerve and sinew\n"
								+"To serve your turn long after they are gone,\n"
								+"And so hold on when there is nothing in you\n"
								+"Except the Will which says to them: 'Hold on!'\n"
								+"\n"
								+"If you can talk with crowds and keep your virtue,\n"
								+"' Or walk with Kings - nor lose the common touch,\n"
								+"if neither foes nor loving friends can hurt you,\n"
								+"If all men count with you, but none too much;\n"
								+"If you can fill the unforgiving minute\n"
								+"With sixty seconds' worth of distance run,\n"
								+"Yours is the Earth and everything that's in it,\n"
								+"And - which is more - you'll be a Man, my son!\n";
				sharesManager.addText("IF - Kippling", if_kipplig);
				sharesManager.addText("Karl Ernst von Baer", "Ses ancêtres venaient de Westphalie. Il fait ses études à Revel et à l'université de Dorpat, ainsi qu'à Berlin, Vienne et Wurtzbourg.\n" +
						"\n" +
						"En 1817, il devient professeur assistant à l'université de Königsberg, professeur de zoologie en 1821, puis d'anatomie en 1826.\n" +
						"\n" +
						"En 1829, il s'installe brièvement à Saint-Pétersbourg avant de revenir à Königsberg. En 1834, il s'établit à Saint-Pétersbourg et rejoint l'Académie des sciences, d'abord en zoologie (de 1834 à 1846) puis en physiologie (de 1846 à 1862). Il dirige le 2e département de la Bibliothèque de l'Académie des sciences de Russie de 1835 à 1862.\n" +
						"\n" +
						"Il s'intéresse à de nombreux sujets comme l'anatomie, l'ichtyologie, l'ethnologie, l'anthropologie et la géographie. C'est le cofondateur et le premier président de la Société entomologique de Russie.\n" +
						"\n" +
						"Il passe la fin de sa vie à Dorpat où il est l'un des critiques les plus virulents des théories darwiniennes.\n" +
						"\n" +
						"Il étudie particulièrement le développement embryonnaires des animaux, découvrant les différents stades de la blastula et de la notochorde. Poursuivant les travaux de Caspar Friedrich Wolff (1734-1794), il décrit avec Christian Heinrich von Pander (1794-1865) le développement de l'embryon à partir de feuillets (ou couches) embryonnaires. Il est à l'origine de la loi de von Baer spécifiant que les caractères embryonnaires qui sont communs à plusieurs taxons animaux apparaissent plus précocement que les caractères distinctifs de ces taxons3.\n" +
						"\n" +
						"Son livre, Über Entwicklungsgeschichte der Tiere, publié en 1828, marque la fondation de l’embryologie comparée.\n" +
						"\n" +
						"Il est également l'auteur en géologie de la loi de Baer.\n" +
						"\n" +
						"Il est lauréat de la Médaille Copley en 1867. Karl Ernst von Baer est devenu membre étranger de la Royal Society le 15 juin 1854.\n" +
						"\n" +
						"C'est son portrait qui est représenté sur les billets de 2 couronnes estoniennes du début des années 1990. Parmi ses étudiants, l'on peut distinguer Adolph Grube.");
				sharesManager.addText(null, "iazppco qspdfpozeoropbpowfobdg");
			}
		}
	}

	public static void addFakeLogEvents(List<EventLog> events, int times) {
		int i;
		final Calendar date = new GregorianCalendar();
		date.roll(Calendar.DAY_OF_MONTH, -1);
		//date.setTime(date.getTime()-(1000*3600*24*2));

		createFakeLogEvent(events, date, 0, EventLog.Severity.INFO, EventLog.Type.SERVER_START_REQUESTED);
		createFakeLogEvent(events, date, 1, EventLog.Severity.INFO, EventLog.Type.SERVER_START_FAILED);
		createFakeLogEvent(events, date, 5, EventLog.Severity.INFO, EventLog.Type.SERVER_START_REQUESTED);
		createFakeLogEvent(events, date, 4, EventLog.Severity.INFO, EventLog.Type.SERVER_STARTED);

		createFakeLogEvent(events, date, 14, EventLog.Severity.INFO, EventLog.Type.USER_ADDED_RESOURCE, "cat_pic.jpeg");
		for (i=0; i<times; i++) {
			//USER_REMOVED_RESOURCE(R.string.eventlog_USER_REMOVED_RESOURCE, Origin.MAIN_ACTIVITY),
			createFakeLogEvent(events, date, "0.0.0.0", 100, EventLog.Severity.ERROR, EventLog.Type.WEBUSER_REQUESTED_UNKOWN_RESOURCE, "bqsazed");
			createFakeLogEvent(events, date, 14, EventLog.Severity.WARN, EventLog.Type.USER_ADD_RESOURCE_DENIED_BECAUSE_DUPLICATED, "cat_pic.jpeg");
			createFakeLogEvent(events, date, "0.0.0.0", 203, EventLog.Severity.INFO, EventLog.Type.WEBUSER_DOWNLOAD_STARTED, "good_music.ogg");
			createFakeLogEvent(events, date, "0.0.0.0", 200, EventLog.Severity.WARN, EventLog.Type.WEBUSER_REQUESTED_FORBIDEN_RESOURCE, "secret.txt");
			createFakeLogEvent(events, date, 124, EventLog.Severity.WARN, EventLog.Type.USER_ADD_RESOURCE_DENIED_BECAUSE_DUPLICATED, "cat_pic.jpeg");
			createFakeLogEvent(events, date, "0.0.0.0", 70, EventLog.Severity.INFO, EventLog.Type.WEBUSER_DOWNLOAD_COMPLETED, "good_music.ogg");
		}
		createFakeLogEvent(events, date, 40, EventLog.Severity.INFO, EventLog.Type.SERVER_STOP_REQUESTED);
		createFakeLogEvent(events, date, 1, EventLog.Severity.INFO, EventLog.Type.SERVER_STOPPED);
	}
	private static void createFakeLogEvent(List<EventLog> events, Calendar date, int secondesLater, EventLog.Severity severity, EventLog.Type type, String ... extras) {
		createFakeLogEvent(events, date, null, secondesLater, severity, type);
	}
	private static void createFakeLogEvent(List<EventLog> events, Calendar date, String user, int secondesLater, EventLog.Severity severity, EventLog.Type type, String ... extras) {
		//date.setTime(date.getTime()+(1000*secondesLater));
		date.roll(Calendar.SECOND, secondesLater);
		events.add(new EventLog(severity, type, new Date(date.getTime().getTime()), user, extras));
	}
}
