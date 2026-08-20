import { useState } from "react";

export default function TestResultModal({

    open,
    onClose,
    test,
    onSave

}) {

    const [form, setForm] = useState({

        sampleType: "",

        sampleDescription: "",

        quantitySampleTaken: "",

        outcome: "",

        observationNotes: "",

        scoreAwarded: "",

        actionTaken: "NONE",

        labReferenceNo: ""

    });

    if (!open || !test) return null;

    function handleChange(e) {

        let value = e.target.value;

        // Sample quantity is a count, so drop anything that is not a digit
        // as it is typed. This also stops "-", "e" and pasted text.
        if (e.target.name === "quantitySampleTaken") {
            value = value.replace(/\D/g, "").slice(0, 3);
        }

        setForm({

            ...form,
            [e.target.name]: value

        });

    }

    function save() {

        if (!form.sampleType) {
            alert("Please select sample type");
            return;
        }

        if (!form.outcome) {
            alert("Please select outcome");
            return;
        }

        const score = String(form.scoreAwarded).trim();

        if (score === "") {
            alert("Please enter score");
            return;
        }

        if (!/^\d+(\.\d+)?$/.test(score)) {
            alert("Score awarded must be a number between 0 and 10");
            return;
        }

        if (Number(score) < 0 || Number(score) > 10) {
            alert("Score awarded must be between 0 and 10");
            return;
        }

        const quantity = String(form.quantitySampleTaken).trim();

        if (quantity === "") {
            alert("Please enter the quantity of sample taken");
            return;
        }

        if (!/^\d+$/.test(quantity)) {
            alert("Quantity of sample taken must be a whole number between 1 and 100");
            return;
        }

        if (Number(quantity) < 1 || Number(quantity) > 100) {
            alert("Quantity of sample taken must be between 1 and 100");
            return;
        }

        onSave({

            testId: test.testId,

            ...form,

            scoreAwarded: Number(form.scoreAwarded)

        });

    }

    return (

        <div className="fixed inset-0 bg-black/40 flex justify-center items-center z-50 p-4">

            <div className="bg-white rounded-xl w-full max-w-[700px] max-h-[90vh] overflow-y-auto p-4 sm:p-6 shadow-xl">

                <h2 className="text-2xl font-bold mb-6">

                    Perform Food Safety Test

                </h2>

                <div className="space-y-4">

                    <div>
                        <b>Product :</b> {test.productName}
                    </div>

                    <div>
                        <b>Test :</b> {test.testTitle}
                    </div>

                    <div>
                        <b>Adulterant :</b> {test.adulterantName}
                    </div>
                     <select
    name="sampleType"
    value={form.sampleType}
    onChange={handleChange}
    className="border p-2 rounded w-full"
>

    <option value="">Select Sample</option>

    <option value="MILK">MILK</option>

    <option value="GHEE">GHEE</option>

    <option value="BUTTER">BUTTER</option>

    <option value="PANEER">PANEER</option>

    <option value="OIL">OIL</option>

    <option value="HONEY">HONEY</option>

    <option value="SUGAR">SUGAR</option>

    <option value="TURMERIC">TURMERIC</option>

    <option value="CHILLI_POWDER">CHILLI POWDER</option>

    <option value="BLACK_PEPPER">BLACK PEPPER</option>

    <option value="CORIANDER_POWDER">CORIANDER POWDER</option>

    <option value="TEA">TEA</option>

    <option value="COFFEE">COFFEE</option>

    <option value="SALT">SALT</option>

    <option value="WHEAT_FLOUR">WHEAT FLOUR</option>

    <option value="RICE">RICE</option>

    <option value="DAL">DAL</option>

    <option value="OTHER">OTHER</option>

</select>
                   

                    <input
                        type="text"
                        name="sampleDescription"
                        value={form.sampleDescription}
                        placeholder="Sample Description"
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    />

                    <input
                        type="text"
                        inputMode="numeric"
                        name="quantitySampleTaken"
                        value={form.quantitySampleTaken}
                        placeholder="Quantity Sample Taken (1 - 100)"
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    />

                    {form.quantitySampleTaken !== "" &&
                        (Number(form.quantitySampleTaken) < 1 ||
                            Number(form.quantitySampleTaken) > 100) && (
                            <p className="text-sm text-red-600 -mt-2">
                                Quantity must be between 1 and 100
                            </p>
                        )}

                    <select
                        name="outcome"
                        value={form.outcome}
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    >

                        <option value="">Select Outcome</option>

                        <option value="PURE">PURE</option>

                        <option value="ADULTERATED">ADULTERATED</option>

                        <option value="INCONCLUSIVE">INCONCLUSIVE</option>

                        <option value="NOT_TESTED">NOT TESTED</option>

                    </select>

                    <textarea
                        name="observationNotes"
                        rows={4}
                        value={form.observationNotes}
                        placeholder="Inspector Observation Notes"
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    />

                    <input
                        type="number"
                        step="0.1"
                        min="0"
                        max="10"
                        name="scoreAwarded"
                        value={form.scoreAwarded}
                        placeholder="Score Awarded (0 - 10)"
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    />

                    {form.scoreAwarded !== "" &&
                        (Number(form.scoreAwarded) < 0 ||
                            Number(form.scoreAwarded) > 10 ||
                            Number.isNaN(Number(form.scoreAwarded))) && (
                            <p className="text-sm text-red-600 -mt-2">
                                Score must be between 0 and 10
                            </p>
                        )}

                    <select
                        name="actionTaken"
                        value={form.actionTaken}
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    >
                     <option value="NONE">NONE</option>
<option value="WARNING">WARNING</option>
<option value="LAB_CONFIRMATION">LAB CONFIRMATION</option>
<option value="SAMPLE_SEIZED">SAMPLE SEIZED</option>
<option value="RE_INSPECTION">RE INSPECTION</option>
<option value="PENALTY">PENALTY</option>
                      

                    </select>

                    <input
                        type="text"
                        name="labReferenceNo"
                        value={form.labReferenceNo}
                        placeholder="Lab Reference Number (Optional)"
                        onChange={handleChange}
                        className="border p-2 rounded w-full"
                    />

                </div>

                <div className="flex justify-end gap-3 mt-6">

                    <button
                        onClick={onClose}
                        className="bg-gray-500 hover:bg-gray-600 text-white px-5 py-2 rounded"
                    >
                        Cancel
                    </button>

                    <button
                        onClick={save}
                        className="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded"
                    >
                        Save Test Result
                    </button>

                </div>

            </div>

        </div>

    );

}