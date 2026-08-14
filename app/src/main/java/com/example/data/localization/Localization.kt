package com.example.data.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    BENGALI("bn", "Bengali", "বাংলা")
}

object Strings {
    private val en = mapOf(
        "app_title" to "GoalAI Planner",
        "app_tagline" to "Dynamic Goal Tracker & AI Scheduler",
        "nav_dashboard" to "Dashboard",
        "nav_schedule" to "Schedule",
        "nav_goals" to "Goals",
        "nav_ai" to "AI Assistant",
        "nav_profile" to "Profile",
        
        // Auth & Setup
        "welcome_back" to "Welcome Back",
        "sign_in_subtitle" to "Track your dreams with intelligent AI scheduling",
        "email_label" to "Email Address",
        "password_label" to "Password",
        "sign_in_button" to "Sign In",
        "sign_up_button" to "Create Account",
        "guest_mode" to "Continue as Guest",
        "guest_disclaimer" to "Full offline features available without account",
        "biometric_prompt_title" to "Biometric Authentication",
        "biometric_prompt_subtitle" to "Touch fingerprint or face scanner to unlock",
        "biometric_unlock_btn" to "Unlock with Biometrics / PIN",
        "or_divider" to "OR",
        "logout" to "Sign Out",
        
        // Onboarding / Profile Setup
        "profile_setup_title" to "Personalize Your AI Coach",
        "name_label" to "Your Name",
        "education_profession_label" to "Field of Study / Profession",
        "education_hint" to "e.g., Computer Science, Medicine, High School, Marketing",
        "daily_hours_label" to "Daily Available Hours",
        "ambition_level_label" to "Goal Ambition Pace",
        "ambition_chill" to "Chill (Steady pace, low stress)",
        "ambition_moderate" to "Moderate (Balanced & productive)",
        "ambition_aggressive" to "Aggressive (High-intensity sprint)",
        "save_profile" to "Save & Continue",
        
        // Dashboard
        "greeting_morning" to "Good morning",
        "greeting_afternoon" to "Good afternoon",
        "greeting_evening" to "Good evening",
        "today_focus" to "Today's Focus & Targets",
        "streak_days" to "Day Streak",
        "active_goals" to "Active Goals",
        "completed_tasks" to "Tasks Done",
        "focus_hours" to "Focus Time",
        "minutes" to "min",
        "hours" to "hrs",
        "quick_actions" to "Quick Actions",
        "btn_add_goal" to "New Goal",
        "btn_add_task" to "Add Task",
        "btn_ai_suggest" to "AI Daily Plan",
        "pomodoro_timer" to "Pomodoro Focus",
        "ai_coach_tip" to "Daily AI Insight",
        "no_tasks_today" to "No tasks scheduled for today. Tap + or let AI create your schedule!",
        "overdue_alert" to "You have overdue tasks. Tap to auto-rebalance your timeline.",
        "rebalance_now" to "Auto-Rebalance with AI",
        
        // Goals
        "goals_header" to "Goals & Milestones",
        "create_goal" to "Create New Goal",
        "goal_title_hint" to "e.g., Pass Exams with A+, Learn Android, Build Portfolio",
        "goal_desc_hint" to "Why is this goal important to you?",
        "target_date" to "Target Completion Date",
        "goal_category" to "Category",
        "milestones_count" to "Milestones",
        "progress" to "Progress",
        "btn_ai_breakdown" to "AI Smart Breakdown",
        "btn_ai_breakdown_desc" to "Let Gemini break this into realistic milestones & bite-sized daily tasks",
        "add_milestone" to "Add Milestone",
        "no_goals" to "No goals yet. Start by creating your first big milestone!",
        
        // Categories
        "cat_study" to "Study & Academics",
        "cat_coding" to "Coding & Tech",
        "cat_career" to "Career & Work",
        "cat_health" to "Health & Fitness",
        "cat_personal" to "Personal Growth",
        
        // Priority
        "priority_high" to "High Priority",
        "priority_medium" to "Medium Priority",
        "priority_low" to "Low Priority",
        
        // Schedule & To-Do
        "schedule_title" to "Dynamic Schedule",
        "today_tab" to "Today",
        "upcoming_tab" to "Upcoming",
        "all_tasks_tab" to "All Tasks",
        "task_notes" to "Task Notes & Checklist",
        "mark_done" to "Mark Complete",
        "task_rolled_over" to "Rescheduled by AI",
        "start_pomodoro" to "Start Focus Timer",
        
        // Pomodoro
        "focus_session" to "Deep Work Focus",
        "short_break" to "Short Break",
        "long_break" to "Long Break",
        "timer_start" to "Start",
        "timer_pause" to "Pause",
        "timer_reset" to "Reset",
        
        // AI Assistant
        "ai_assistant_title" to "Dynamic AI Schedule Engine",
        "ai_chat_placeholder" to "Ask AI to plan, lighten, rebalance, or explain...",
        "ai_chip_sick" to "I was sick yesterday, rebalance missed tasks",
        "ai_chip_lighten" to "Make today lighter (only 1 hour available)",
        "ai_chip_breakdown" to "Break down my top priority goal into tasks",
        "ai_chip_exam" to "Give me an intensive 7-day study strategy",
        "ai_chip_pomodoro" to "Optimize my day with Pomodoro intervals",
        "apply_ai_changes" to "Apply AI Schedule to Database",
        "ai_changes_applied" to "Schedule successfully updated in local database!",
        "ai_thinking" to "Gemini AI is analyzing your pace and goals...",
        
        // Profile & Settings
        "profile_title" to "Profile & Preferences",
        "language_setting" to "Language (ভাষা)",
        "english" to "English",
        "bengali" to "বাংলা (Bengali)",
        "biometric_security" to "Biometric & App Lock",
        "dark_mode" to "Dark Glassmorphic Mode",
        "analytics_overview" to "Weekly Productivity Analytics",
        "weekly_completion_rate" to "Weekly Completion Rate",
        "study_hours_logged" to "Total Study/Work Hours",
        "export_data" to "Offline Room Database Active",
        "saved_locally" to "100% Offline-First with Cloud AI Sync"
    )

    private val bn = mapOf(
        "app_title" to "গোল এআই প্ল্যানার",
        "app_tagline" to "স্মার্ট লক্ষ্য ট্র্যাকার ও এআই শিডিউলার",
        "nav_dashboard" to "ড্যাশবোর্ড",
        "nav_schedule" to "সময়সূচি",
        "nav_goals" to "লক্ষ্যসমূহ",
        "nav_ai" to "এআই সহকারী",
        "nav_profile" to "প্রোফাইল",
        
        // Auth & Setup
        "welcome_back" to "স্বাগতম",
        "sign_in_subtitle" to "স্মার্ট এআই শিডিউলিং দিয়ে আপনার লক্ষ্য পূরণ করুন",
        "email_label" to "ইমেইল ঠিকানা",
        "password_label" to "পাসওয়ার্ড",
        "sign_in_button" to "লগইন করুন",
        "sign_up_button" to "নতুন অ্যাকাউন্ট তৈরি করুন",
        "guest_mode" to "গেস্ট হিসেবে ব্যবহার করুন",
        "guest_disclaimer" to "অ্যাকাউন্ট ছাড়াই সম্পূর্ণ অফলাইন সুবিধা পাবেন",
        "biometric_prompt_title" to "বায়োমেট্রিক সিকিউরিটি",
        "biometric_prompt_subtitle" to "আঙুলের ছাপ বা ফেস দিয়ে আনলক করুন",
        "biometric_unlock_btn" to "বায়োমেট্রিক / পিন দিয়ে আনলক",
        "or_divider" to "অথবা",
        "logout" to "সাইন আউট",
        
        // Onboarding / Profile Setup
        "profile_setup_title" to "আপনার এআই সহকারী কাস্টমাইজ করুন",
        "name_label" to "আপনার নাম",
        "education_profession_label" to "পড়াশোনা বা পেশার ক্ষেত্র",
        "education_hint" to "যেমন: কম্পিউটার সায়েন্স, মেডিকেল, স্কুল, মার্কেটিং",
        "daily_hours_label" to "দৈনিক কাজের/পড়ার সময় (ঘণ্টা)",
        "ambition_level_label" to "লক্ষ্য অর্জনের তীব্রতা",
        "ambition_chill" to "ধীরস্থির (সহজ গতি, কম চাপ)",
        "ambition_moderate" to "ভারসাম্যপূর্ণ (নিয়মিত ও উৎপাদনশীল)",
        "ambition_aggressive" to "তীব্র গতি (সর্বোচ্চ স্প্রিন্ট ও ফোকাস)",
        "save_profile" to "সংরক্ষণ করুন",
        
        // Dashboard
        "greeting_morning" to "শুভ সকাল",
        "greeting_afternoon" to "শুভ দুপুর",
        "greeting_evening" to "শুভ সন্ধ্যা",
        "today_focus" to "আজকের ফোকাস ও টাস্ক",
        "streak_days" to "দিনের স্ট্রাইক",
        "active_goals" to "সক্রিয় লক্ষ্য",
        "completed_tasks" to "সম্পন্ন কাজ",
        "focus_hours" to "ফোকাস সময়",
        "minutes" to "মিনিট",
        "hours" to "ঘণ্টা",
        "quick_actions" to "দ্রুত অ্যাকশন",
        "btn_add_goal" to "নতুন লক্ষ্য",
        "btn_add_task" to "টাস্ক যোগ করুন",
        "btn_ai_suggest" to "এআই দৈনিক প্ল্যান",
        "pomodoro_timer" to "পোমোডোরো ফোকাস",
        "ai_coach_tip" to "দৈনিক এআই পরামর্শ",
        "no_tasks_today" to "আজ কোনো টাস্ক নেই। + চাপুন বা এআই দিয়ে শিডিউল তৈরি করুন!",
        "overdue_alert" to "কিছু কাজ বাকি রয়ে গেছে। এআই দিয়ে সময়সূচি পুনর্বিন্যাস করুন।",
        "rebalance_now" to "এআই দিয়ে সময়সূচি সাজান",
        
        // Goals
        "goals_header" to "লক্ষ্য ও মাইলস্টোন",
        "create_goal" to "নতুন লক্ষ্য তৈরি করুন",
        "goal_title_hint" to "যেমন: পরীক্ষায় A+ পাওয়া, অ্যান্ড্রয়েড শেখা, পোর্টফোলিও তৈরি",
        "goal_desc_hint" to "এই লক্ষ্যটি আপনার জন্য কেন গুরুত্বপূর্ণ?",
        "target_date" to "লক্ষ্য পূরণের শেষ তারিখ",
        "goal_category" to "ক্যাটাগরি",
        "milestones_count" to "মাইলস্টোন",
        "progress" to "অগ্রগতি",
        "btn_ai_breakdown" to "এআই স্মার্ট বিভাজন",
        "btn_ai_breakdown_desc" to "জেমিনি এআই আপনার লক্ষ্যকে বাস্তবসম্মত মাইলস্টোন ও দৈনন্দিন কাজে ভাগ করবে",
        "add_milestone" to "মাইলস্টোন যোগ করুন",
        "no_goals" to "এখনো কোনো লক্ষ্য যোগ করা হয়নি। প্রথম বড় লক্ষ্যটি শুরু করুন!",
        
        // Categories
        "cat_study" to "পড়াশোনা ও শিক্ষা",
        "cat_coding" to "কোডিং ও প্রযুক্তি",
        "cat_career" to "ক্যারিয়ার ও চাকরি",
        "cat_health" to "স্বাস্থ্য ও ফিটনেস",
        "cat_personal" to "ব্যক্তিগত উন্নয়ন",
        
        // Priority
        "priority_high" to "জরুরি (High)",
        "priority_medium" to "মাঝারি (Medium)",
        "priority_low" to "সাধারণ (Low)",
        
        // Schedule & To-Do
        "schedule_title" to "ডাইনামিক শিডিউল",
        "today_tab" to "আজ",
        "upcoming_tab" to "আসন্ন",
        "all_tasks_tab" to "সব কাজ",
        "task_notes" to "নোট ও চেকলিস্ট",
        "mark_done" to "সম্পন্ন করুন",
        "task_rolled_over" to "এআই দ্বারা পুনঃনির্ধারিত",
        "start_pomodoro" to "ফোকাস টাইমার চালু করুন",
        
        // Pomodoro
        "focus_session" to "গভীর মনোযোগের কাজ",
        "short_break" to "ছোট বিরতি",
        "long_break" to "বড় বিরতি",
        "timer_start" to "শুরু",
        "timer_pause" to "বিরতি",
        "timer_reset" to "রিসেট",
        
        // AI Assistant
        "ai_assistant_title" to "ডাইনামিক এআই শিডিউল ইঞ্জিন",
        "ai_chat_placeholder" to "এআই-কে প্ল্যান করতে বা সময়সূচি পরিবর্তন করতে বলুন...",
        "ai_chip_sick" to "গতকাল অসুস্থ ছিলাম, বাকি কাজগুলো নতুন করে সাজিয়ে দাও",
        "ai_chip_lighten" to "আজকের চাপ কমাও (মাত্র ১ ঘণ্টা সময় আছে)",
        "ai_chip_breakdown" to "আমার প্রধান লক্ষ্যকে ছোট ছোট কাজে ভাগ করো",
        "ai_chip_exam" to "পরীক্ষার প্রস্তুতির জন্য ৭ দিনের নিবিড় কৌশল দাও",
        "ai_chip_pomodoro" to "পোমোডোরো বিরতি সহ আজকের দিনটি সাজাও",
        "apply_ai_changes" to "এআই সময়সূচি ডাটাবেসে যোগ করুন",
        "ai_changes_applied" to "লোকাল ডাটাবেসে সময়সূচি সফলভাবে হালনাগাদ হয়েছে!",
        "ai_thinking" to "জেমিনি এআই আপনার রুটিন ও গতি বিশ্লেষণ করছে...",
        
        // Profile & Settings
        "profile_title" to "প্রোফাইল ও সেটিংস",
        "language_setting" to "ভাষা (Language)",
        "english" to "English (ইংরেজি)",
        "bengali" to "বাংলা (Bengali)",
        "biometric_security" to "বায়োমেট্রিক ও অ্যাপ লক",
        "dark_mode" to "ডার্ক গ্লাসমরফিক মোড",
        "analytics_overview" to "সাপ্তাহিক উৎপাদনশীলতা চার্ট",
        "weekly_completion_rate" to "সাপ্তাহিক কাজ সম্পন্নের হার",
        "study_hours_logged" to "মোট পড়াশোনা/কাজের সময়",
        "export_data" to "অফলাইন রুম ডাটাবেস সক্রিয়",
        "saved_locally" to "১০০% অফলাইন সুবিধা সহ এআই ক্লাউড সংযুক্ত"
    )

    fun get(key: String, lang: AppLanguage = AppLanguage.ENGLISH): String {
        return when (lang) {
            AppLanguage.BENGALI -> bn[key] ?: en[key] ?: key
            AppLanguage.ENGLISH -> en[key] ?: key
        }
    }
}
