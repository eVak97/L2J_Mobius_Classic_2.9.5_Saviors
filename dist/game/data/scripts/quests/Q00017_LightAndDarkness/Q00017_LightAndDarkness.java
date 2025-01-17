/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q00017_LightAndDarkness;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00017_LightAndDarkness extends Quest
{
	// NPCs
	private static final int HIERARCH = 31517;
	private static final int SAINT_ALTAR_1 = 31508;
	private static final int SAINT_ALTAR_2 = 31509;
	private static final int SAINT_ALTAR_3 = 31510;
	private static final int SAINT_ALTAR_4 = 31511;
	// Items
	private static final int BLOOD_OF_SAINT = 7168;
	
	public Q00017_LightAndDarkness()
	{
		super(17);
		registerQuestItems(BLOOD_OF_SAINT);
		addStartNpc(HIERARCH);
		addTalkId(HIERARCH, SAINT_ALTAR_1, SAINT_ALTAR_2, SAINT_ALTAR_3, SAINT_ALTAR_4);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "31517-04.htm":
			{
				st.startQuest();
				giveItems(player, BLOOD_OF_SAINT, 4);
				break;
			}
			case "31508-02.htm":
			{
				if (hasQuestItems(player, BLOOD_OF_SAINT))
				{
					st.setCond(2, true);
					takeItems(player, BLOOD_OF_SAINT, 1);
				}
				else
				{
					htmltext = "31508-03.htm";
				}
				break;
			}
			case "31509-02.htm":
			{
				if (hasQuestItems(player, BLOOD_OF_SAINT))
				{
					st.setCond(3, true);
					takeItems(player, BLOOD_OF_SAINT, 1);
				}
				else
				{
					htmltext = "31509-03.htm";
				}
				break;
			}
			case "31510-02.htm":
			{
				if (hasQuestItems(player, BLOOD_OF_SAINT))
				{
					st.setCond(4, true);
					takeItems(player, BLOOD_OF_SAINT, 1);
				}
				else
				{
					htmltext = "31510-03.htm";
				}
				break;
			}
			case "31511-02.htm":
			{
				if (hasQuestItems(player, BLOOD_OF_SAINT))
				{
					st.setCond(5, true);
					takeItems(player, BLOOD_OF_SAINT, 1);
				}
				else
				{
					htmltext = "31511-03.htm";
				}
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = (player.getLevel() < 61) ? "31517-03.htm" : "31517-01.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case HIERARCH:
					{
						if (cond == 5)
						{
							htmltext = "31517-07.htm";
							addExpAndSp(player, 105527, 0);
							st.exitQuest(false, true);
						}
						else
						{
							if (hasQuestItems(player, BLOOD_OF_SAINT))
							{
								htmltext = "31517-05.htm";
							}
							else
							{
								htmltext = "31517-06.htm";
								st.exitQuest(true, true);
							}
						}
						break;
					}
					case SAINT_ALTAR_1:
					{
						if (cond == 1)
						{
							htmltext = "31508-01.htm";
						}
						else if (cond > 1)
						{
							htmltext = "31508-04.htm";
						}
						break;
					}
					case SAINT_ALTAR_2:
					{
						if (cond == 2)
						{
							htmltext = "31509-01.htm";
						}
						else if (cond > 2)
						{
							htmltext = "31509-04.htm";
						}
						break;
					}
					case SAINT_ALTAR_3:
					{
						if (cond == 3)
						{
							htmltext = "31510-01.htm";
						}
						else if (cond > 3)
						{
							htmltext = "31510-04.htm";
						}
						break;
					}
					case SAINT_ALTAR_4:
					{
						if (cond == 4)
						{
							htmltext = "31511-01.htm";
						}
						else if (cond > 4)
						{
							htmltext = "31511-04.htm";
						}
						break;
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		
		return htmltext;
	}
}