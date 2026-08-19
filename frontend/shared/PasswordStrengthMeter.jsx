/**
 * Password strength meter shared by every GutFriendly signup form.
 *
 * Styled with inline styles on purpose: userside ships hand-written CSS while
 * the vendor and staff portals use Tailwind, and this component has to look
 * the same in all of them without either stylesheet being involved.
 *
 * Pass it the result of evaluatePassword() from "@shared/validation".
 */

const LEVEL_COLORS = {
    weak: "#dc2626",
    medium: "#d97706",
    strong: "#16a34a"
};

const TRACK_STYLE = {
    height: "6px",
    borderRadius: "999px",
    backgroundColor: "#e5e7eb",
    overflow: "hidden"
};

export default function PasswordStrengthMeter({ evaluation, showChecklist = true }) {
    if (!evaluation || evaluation.level === "empty") {
        return null;
    }

    const color = LEVEL_COLORS[evaluation.level] || LEVEL_COLORS.weak;

    return (
        <div style={{ marginTop: "8px" }} aria-live="polite">

            <div style={TRACK_STYLE}>
                <div
                    style={{
                        width: evaluation.percent + "%",
                        height: "100%",
                        backgroundColor: color,
                        transition: "width 160ms ease, background-color 160ms ease"
                    }}
                />
            </div>

            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    gap: "12px",
                    marginTop: "6px",
                    fontSize: "12px"
                }}
            >
                <span style={{ color, fontWeight: 600 }}>
                    {evaluation.label}
                </span>

                {!evaluation.isAcceptable && (
                    <span style={{ color: "#6b7280" }}>
                        Medium or better required
                    </span>
                )}
            </div>

            {showChecklist && (
                <ul
                    style={{
                        listStyle: "none",
                        margin: "8px 0 0",
                        padding: 0,
                        display: "grid",
                        gap: "3px",
                        fontSize: "12px"
                    }}
                >
                    {evaluation.rules.map((rule) => (
                        <li
                            key={rule.id}
                            style={{
                                color: rule.met ? "#16a34a" : "#6b7280",
                                display: "flex",
                                alignItems: "center",
                                gap: "6px"
                            }}
                        >
                            <span aria-hidden="true">
                                {rule.met ? "\u2713" : "\u25cb"}
                            </span>
                            {rule.label}
                        </li>
                    ))}
                </ul>
            )}

        </div>
    );
}
