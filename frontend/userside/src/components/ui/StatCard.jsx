import React from "react";

/**
 * Modern stat card — soft tinted icon badge, clean type, subtle lift on hover.
 * Pass a `tone` key instead of a raw Tailwind bg class; each tone maps to a
 * matched { badgeBg, badgeText, ring } set so the icon chip and card accent
 * always stay in the same color family (mirrors the soft pastel chips in
 * the reference screenshot, e.g. the amber "Reward points" card).
 */

const TONES = {
    emerald: {
        badgeBg: "bg-emerald-50",
        badgeText: "text-emerald-600",
        ring: "ring-emerald-100",
        accent: "bg-emerald-500",
    },
    blue: {
        badgeBg: "bg-blue-50",
        badgeText: "text-blue-600",
        ring: "ring-blue-100",
        accent: "bg-blue-500",
    },
    amber: {
        badgeBg: "bg-amber-50",
        badgeText: "text-amber-600",
        ring: "ring-amber-100",
        accent: "bg-amber-500",
    },
    rose: {
        badgeBg: "bg-rose-50",
        badgeText: "text-rose-600",
        ring: "ring-rose-100",
        accent: "bg-rose-500",
    },
    violet: {
        badgeBg: "bg-violet-50",
        badgeText: "text-violet-600",
        ring: "ring-violet-100",
        accent: "bg-violet-500",
    },
    pink: {
        badgeBg: "bg-pink-50",
        badgeText: "text-pink-600",
        ring: "ring-pink-100",
        accent: "bg-pink-500",
    },
};

export default function StatCard({
    title,
    value,
    icon,
    tone = "emerald",
    trend, // optional: { direction: "up" | "down", label: "+12% this week" }
}) {
    const t = TONES[tone] ?? TONES.emerald;

    return (
        <div className="group relative bg-white rounded-2xl border border-slate-100 shadow-sm p-6 transition-all duration-200 hover:shadow-md hover:-translate-y-0.5">

            {/* thin accent bar, only reveals on hover — keeps the resting state quiet */}
            <div
                className={`absolute inset-x-6 top-0 h-0.5 rounded-full ${t.accent} opacity-0 group-hover:opacity-100 transition-opacity duration-200`}
            />

            <div className="flex items-start justify-between gap-4">

                <div className="min-w-0">
                    <p className="text-sm font-medium text-slate-500 truncate">
                        {title}
                    </p>

                    <h2 className="text-3xl font-bold text-slate-900 mt-2 tracking-tight">
                        {value}
                    </h2>

                    {trend && (
                        <p
                            className={`mt-2 inline-flex items-center gap-1 text-xs font-semibold ${
                                trend.direction === "up" ? "text-emerald-600" : "text-rose-600"
                            }`}
                        >
                            <span aria-hidden="true">
                                {trend.direction === "up" ? "↑" : "↓"}
                            </span>
                            {trend.label}
                        </p>
                    )}
                </div>

                <div
                    className={`shrink-0 flex items-center justify-center w-12 h-12 rounded-xl ${t.badgeBg} ${t.badgeText} ring-1 ${t.ring} text-xl`}
                >
                    {icon}
                </div>

            </div>

        </div>
    );
}
